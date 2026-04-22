const state = {
    currentView: "dashboard",
    departments: [],
    employeeRows: [],
    employeeTotal: 0,
    selectedEmployeeIds: new Set(),
    filters: {
        page: 1,
        pageSize: 10
    }
};

const elements = {
    statusPill: document.getElementById("statusPill"),
    deptCount: document.getElementById("deptCount"),
    empCount: document.getElementById("empCount"),
    femaleCount: document.getElementById("femaleCount"),
    exprCount: document.getElementById("exprCount"),
    deptDistribution: document.getElementById("deptDistribution"),
    recentEmployees: document.getElementById("recentEmployees"),
    deptTableBody: document.getElementById("deptTableBody"),
    empTableBody: document.getElementById("empTableBody"),
    pageSummary: document.getElementById("pageSummary"),
    pageNumber: document.getElementById("pageNumber"),
    filterForm: document.getElementById("filterForm"),
    filterDeptId: document.getElementById("filterDeptId"),
    selectAllEmps: document.getElementById("selectAllEmps"),
    deptModal: document.getElementById("deptModal"),
    empModal: document.getElementById("empModal"),
    detailModal: document.getElementById("detailModal"),
    deptForm: document.getElementById("deptForm"),
    empForm: document.getElementById("empForm"),
    deptModalTitle: document.getElementById("deptModalTitle"),
    empModalTitle: document.getElementById("empModalTitle"),
    detailContent: document.getElementById("detailContent"),
    empDeptId: document.getElementById("empDeptId"),
    exprContainer: document.getElementById("exprContainer"),
    exprTemplate: document.getElementById("exprTemplate")
};

document.addEventListener("DOMContentLoaded", () => {
    bindEvents();
    initializeApp();
});

function bindEvents() {
    document.querySelectorAll(".nav-link").forEach((button) => {
        button.addEventListener("click", () => switchView(button.dataset.view));
    });

    document.getElementById("refreshAllBtn").addEventListener("click", initializeApp);
    document.getElementById("addDeptBtn").addEventListener("click", () => openDeptModal());
    document.getElementById("addEmpBtn").addEventListener("click", () => openEmpModal());
    document.getElementById("resetFilterBtn").addEventListener("click", resetFilters);
    document.getElementById("prevPageBtn").addEventListener("click", () => changePage(-1));
    document.getElementById("nextPageBtn").addEventListener("click", () => changePage(1));
    document.getElementById("batchDeleteBtn").addEventListener("click", batchDeleteEmployees);
    document.getElementById("addExprBtn").addEventListener("click", () => appendExprItem());

    elements.filterForm.addEventListener("submit", (event) => {
        event.preventDefault();
        state.filters = {
            ...state.filters,
            ...readFilterValues(),
            page: 1
        };
        loadEmployees();
    });

    elements.deptForm.addEventListener("submit", submitDeptForm);
    elements.empForm.addEventListener("submit", submitEmpForm);
    elements.selectAllEmps.addEventListener("change", toggleSelectAll);

    document.querySelectorAll("[data-close]").forEach((button) => {
        button.addEventListener("click", () => closeModal(button.dataset.close));
    });

    [elements.deptModal, elements.empModal, elements.detailModal].forEach((modal) => {
        modal.addEventListener("click", (event) => {
            if (event.target === modal) {
                modal.classList.add("hidden");
            }
        });
    });
}

async function initializeApp() {
    try {
        setStatus("同步数据中...");
        await loadDepartments();
        await loadEmployees({ page: 1 });
        await renderDashboard();
        setStatus("数据已更新");
    } catch (error) {
        handleError(error);
    }
}

function switchView(view) {
    state.currentView = view;
    document.querySelectorAll(".nav-link").forEach((button) => {
        button.classList.toggle("active", button.dataset.view === view);
    });
    document.querySelectorAll(".view").forEach((section) => {
        section.classList.toggle("active", section.id === `${view}View`);
    });
}

async function apiRequest(url, options = {}) {
    const finalOptions = {
        ...options,
        headers: {
            ...(options.body ? { "Content-Type": "application/json" } : {}),
            ...(options.headers || {})
        }
    };
    const response = await fetch(url, finalOptions);

    let result;
    try {
        result = await response.json();
    } catch (error) {
        throw new Error("接口返回内容不是有效的 JSON");
    }

    if (!response.ok || result.code !== 1) {
        throw new Error(result.msg || "请求失败");
    }
    return result.data;
}

function field(form, name) {
    return form.querySelector(`[name="${name}"]`);
}

async function loadDepartments() {
    const selectedFilterDept = elements.filterDeptId.value;
    const selectedFormDept = elements.empDeptId.value;

    const departments = await apiRequest("/depts");
    state.departments = Array.isArray(departments) ? departments : [];

    renderDepartmentOptions();
    elements.filterDeptId.value = selectedFilterDept;
    elements.empDeptId.value = selectedFormDept;
    renderDepartmentTable();
}

async function loadEmployees(overrides = {}) {
    state.filters = {
        ...state.filters,
        ...overrides
    };

    const params = new URLSearchParams();
    Object.entries(state.filters).forEach(([key, value]) => {
        if (value !== "" && value !== null && value !== undefined) {
            params.set(key, value);
        }
    });

    const pageData = await apiRequest(`/emps?${params.toString()}`);
    state.employeeRows = pageData?.rows || [];
    state.employeeTotal = pageData?.total || 0;
    state.selectedEmployeeIds.clear();
    renderEmployeeTable();
    renderPagination();
}

async function renderDashboard() {
    const allEmployees = await fetchAllEmployees();
    const femaleCount = allEmployees.filter((item) => Number(item.gender) === 2).length;

    const detailResults = await Promise.all(
        allEmployees.map(async (employee) => {
            try {
                return await apiRequest(`/emps/${employee.id}`);
            } catch (error) {
                return null;
            }
        })
    );

    const exprCount = detailResults.filter((item) => Array.isArray(item?.exprList) && item.exprList.length > 0).length;

    elements.deptCount.textContent = String(state.departments.length);
    elements.empCount.textContent = String(allEmployees.length);
    elements.femaleCount.textContent = String(femaleCount);
    elements.exprCount.textContent = String(exprCount);

    renderDepartmentDistribution(allEmployees);
    renderRecentEmployees(allEmployees);
}

async function fetchAllEmployees() {
    const pageData = await apiRequest("/emps?page=1&pageSize=1000");
    return pageData?.rows || [];
}

function renderDepartmentDistribution(allEmployees) {
    if (!allEmployees.length) {
        elements.deptDistribution.innerHTML = '<div class="empty-state">暂无统计数据</div>';
        return;
    }

    const grouped = state.departments
        .map((dept) => ({
            name: dept.name,
            count: allEmployees.filter((employee) => Number(employee.deptId) === Number(dept.id)).length
        }))
        .sort((a, b) => b.count - a.count);

    const maxCount = grouped[0]?.count || 1;
    elements.deptDistribution.innerHTML = grouped.map((item) => `
        <div class="bar-item">
            <div class="meta-row">
                <strong>${escapeHtml(item.name)}</strong>
                <span>${item.count} 人</span>
            </div>
            <div class="bar-track">
                <div class="bar-fill" style="width:${(item.count / maxCount) * 100}%"></div>
            </div>
        </div>
    `).join("");
}

function renderRecentEmployees(allEmployees) {
    const recent = [...allEmployees]
        .sort((a, b) => (b.entryDate || "").localeCompare(a.entryDate || ""))
        .slice(0, 6);

    if (!recent.length) {
        elements.recentEmployees.innerHTML = '<div class="empty-state">暂无员工数据</div>';
        return;
    }

    elements.recentEmployees.innerHTML = recent.map((item) => `
        <div class="simple-list-item">
            <div>
                <strong>${escapeHtml(item.name || "-")}</strong>
                <p>${escapeHtml(item.deptName || "未分配部门")}</p>
            </div>
            <span class="tag">${formatDate(item.entryDate)}</span>
        </div>
    `).join("");
}

function renderDepartmentOptions() {
    const options = state.departments
        .map((dept) => `<option value="${dept.id}">${escapeHtml(dept.name)}</option>`)
        .join("");

    elements.filterDeptId.innerHTML = '<option value="">全部部门</option>' + options;
    elements.empDeptId.innerHTML = '<option value="">请选择部门</option>' + options;
}

function renderDepartmentTable() {
    if (!state.departments.length) {
        elements.deptTableBody.innerHTML = '<tr><td colspan="5" class="empty-state">暂无部门数据</td></tr>';
        return;
    }

    elements.deptTableBody.innerHTML = state.departments.map((dept) => `
        <tr>
            <td>${dept.id}</td>
            <td>${escapeHtml(dept.name)}</td>
            <td>${formatDateTime(dept.createTime)}</td>
            <td>${formatDateTime(dept.updateTime)}</td>
            <td>
                <div class="inline-actions">
                    <button class="text-btn" onclick="editDepartment(${dept.id})">编辑</button>
                    <button class="text-btn danger-btn" onclick="deleteDepartment(${dept.id})">删除</button>
                </div>
            </td>
        </tr>
    `).join("");
}

function renderEmployeeTable() {
    if (!state.employeeRows.length) {
        elements.empTableBody.innerHTML = '<tr><td colspan="9" class="empty-state">暂无员工数据</td></tr>';
        elements.selectAllEmps.checked = false;
        return;
    }

    elements.empTableBody.innerHTML = state.employeeRows.map((emp) => `
        <tr>
            <td><input type="checkbox" ${state.selectedEmployeeIds.has(emp.id) ? "checked" : ""} onchange="toggleEmployeeSelection(${emp.id}, this.checked)"></td>
            <td>${emp.id}</td>
            <td>${escapeHtml(emp.username || "-")}</td>
            <td>${escapeHtml(emp.name || "-")}</td>
            <td>${genderText(emp.gender)}</td>
            <td>${escapeHtml(emp.phone || "-")}</td>
            <td>${escapeHtml(emp.deptName || "未分配")}</td>
            <td>${formatDate(emp.entryDate)}</td>
            <td>
                <div class="inline-actions">
                    <button class="text-btn" onclick="viewEmployeeDetail(${emp.id})">详情</button>
                    <button class="text-btn" onclick="editEmployee(${emp.id})">编辑</button>
                    <button class="text-btn danger-btn" onclick="deleteEmployee(${emp.id})">删除</button>
                </div>
            </td>
        </tr>
    `).join("");

    elements.selectAllEmps.checked = false;
}

function renderPagination() {
    const page = Number(state.filters.page || 1);
    const pageSize = Number(state.filters.pageSize || 10);
    const totalPages = Math.max(1, Math.ceil(state.employeeTotal / pageSize));
    elements.pageSummary.textContent = `共 ${state.employeeTotal} 条，当前每页 ${pageSize} 条`;
    elements.pageNumber.textContent = `${page} / ${totalPages}`;
    document.getElementById("prevPageBtn").disabled = page <= 1;
    document.getElementById("nextPageBtn").disabled = page >= totalPages;
}

function openDeptModal(dept = null) {
    elements.deptForm.reset();
    elements.deptModalTitle.textContent = dept ? "编辑部门" : "新增部门";
    field(elements.deptForm, "id").value = dept?.id || "";
    field(elements.deptForm, "name").value = dept?.name || "";
    elements.deptModal.classList.remove("hidden");
}

function openEmpModal(emp = null) {
    elements.empForm.reset();
    elements.exprContainer.innerHTML = "";
    elements.empModalTitle.textContent = emp ? "编辑员工" : "新增员工";

    field(elements.empForm, "id").value = emp?.id || "";
    field(elements.empForm, "username").value = emp?.username || "";
    field(elements.empForm, "password").value = emp?.password || "123456";
    field(elements.empForm, "name").value = emp?.name || "";
    field(elements.empForm, "phone").value = emp?.phone || "";
    field(elements.empForm, "gender").value = String(emp?.gender || 1);
    field(elements.empForm, "entryDate").value = normalizeDateInput(emp?.entryDate);
    elements.empDeptId.value = emp?.deptId ? String(emp.deptId) : "";

    const exprList = Array.isArray(emp?.exprList) && emp.exprList.length ? emp.exprList : [null];
    exprList.forEach((expr) => appendExprItem(expr));

    elements.empModal.classList.remove("hidden");
}

function appendExprItem(expr = null) {
    const node = elements.exprTemplate.content.firstElementChild.cloneNode(true);

    node.querySelector('[data-field="company"]').value = expr?.company || "";
    node.querySelector('[data-field="profession"]').value = expr?.profession || "";
    node.querySelector('[data-field="begin"]').value = normalizeDateInput(expr?.begin);
    node.querySelector('[data-field="end"]').value = normalizeDateInput(expr?.end);

    node.querySelector(".remove-expr-btn").addEventListener("click", () => {
        node.remove();
    });
    elements.exprContainer.appendChild(node);
}

function closeModal(id) {
    document.getElementById(id).classList.add("hidden");
}

async function submitDeptForm(event) {
    event.preventDefault();

    const id = field(elements.deptForm, "id").value;
    const payload = {
        id: id ? Number(id) : undefined,
        name: field(elements.deptForm, "name").value.trim()
    };

    try {
        setStatus(id ? "正在更新部门..." : "正在新增部门...");
        await apiRequest("/depts", {
            method: id ? "PUT" : "POST",
            body: JSON.stringify(payload)
        });
        closeModal("deptModal");
        await loadDepartments();
        await loadEmployees();
        await renderDashboard();
        setStatus("部门保存成功");
    } catch (error) {
        handleError(error);
    }
}

async function submitEmpForm(event) {
    event.preventDefault();

    const payload = buildEmployeePayload();
    const id = field(elements.empForm, "id").value;

    try {
        setStatus(id ? "正在更新员工..." : "正在新增员工...");
        await apiRequest("/emps", {
            method: id ? "PUT" : "POST",
            body: JSON.stringify(id ? { ...payload, id: Number(id) } : payload)
        });
        closeModal("empModal");
        await loadEmployees();
        await renderDashboard();
        setStatus("员工保存成功");
    } catch (error) {
        handleError(error);
    }
}

function buildEmployeePayload() {
    const exprList = Array.from(elements.exprContainer.querySelectorAll(".expr-item"))
        .map((item) => ({
            company: item.querySelector('[data-field="company"]').value.trim(),
            profession: item.querySelector('[data-field="profession"]').value.trim(),
            begin: item.querySelector('[data-field="begin"]').value || null,
            end: item.querySelector('[data-field="end"]').value || null
        }))
        .filter((item) => item.company || item.profession || item.begin || item.end);

    return {
        username: field(elements.empForm, "username").value.trim(),
        password: field(elements.empForm, "password").value.trim(),
        name: field(elements.empForm, "name").value.trim(),
        phone: field(elements.empForm, "phone").value.trim(),
        gender: Number(field(elements.empForm, "gender").value),
        deptId: Number(elements.empDeptId.value),
        entryDate: field(elements.empForm, "entryDate").value,
        exprList
    };
}

function readFilterValues() {
    return {
        name: field(elements.filterForm, "name").value.trim(),
        gender: field(elements.filterForm, "gender").value,
        deptId: field(elements.filterForm, "deptId").value,
        begin: field(elements.filterForm, "begin").value,
        end: field(elements.filterForm, "end").value
    };
}

function resetFilters() {
    elements.filterForm.reset();
    state.filters = { page: 1, pageSize: 10 };
    loadEmployees();
}

function changePage(step) {
    const page = Number(state.filters.page || 1) + step;
    if (page < 1) {
        return;
    }
    loadEmployees({ page });
}

function toggleSelectAll(event) {
    state.selectedEmployeeIds.clear();
    if (event.target.checked) {
        state.employeeRows.forEach((item) => state.selectedEmployeeIds.add(item.id));
    }
    renderEmployeeTable();
}

window.toggleEmployeeSelection = function toggleEmployeeSelection(id, checked) {
    if (checked) {
        state.selectedEmployeeIds.add(id);
    } else {
        state.selectedEmployeeIds.delete(id);
    }
};

window.editDepartment = function editDepartment(id) {
    const dept = state.departments.find((item) => Number(item.id) === Number(id));
    if (dept) {
        openDeptModal(dept);
    }
};

window.deleteDepartment = async function deleteDepartment(id) {
    if (!window.confirm("确认删除这个部门吗？")) {
        return;
    }

    try {
        setStatus("正在删除部门...");
        await apiRequest(`/depts/${id}`, { method: "DELETE" });
        await loadDepartments();
        await loadEmployees();
        await renderDashboard();
        setStatus("部门已删除");
    } catch (error) {
        handleError(error);
    }
};

window.viewEmployeeDetail = async function viewEmployeeDetail(id) {
    try {
        setStatus("正在加载员工详情...");
        const emp = await apiRequest(`/emps/${id}`);
        const exprHtml = Array.isArray(emp.exprList) && emp.exprList.length
            ? emp.exprList.map((item) => `
                <div class="detail-card">
                    <strong>${escapeHtml(item.company || "未填写公司")}</strong>
                    <p>${escapeHtml(item.profession || "未填写岗位")}</p>
                    <p>${formatDate(item.begin)} - ${formatDate(item.end)}</p>
                </div>
            `).join("")
            : '<div class="detail-card">暂无工作经历</div>';

        elements.detailContent.innerHTML = `
            <div class="detail-card">
                <div class="detail-meta">
                    <div><p>姓名</p><strong>${escapeHtml(emp.name || "-")}</strong></div>
                    <div><p>用户名</p><strong>${escapeHtml(emp.username || "-")}</strong></div>
                    <div><p>性别</p><strong>${genderText(emp.gender)}</strong></div>
                    <div><p>手机号</p><strong>${escapeHtml(emp.phone || "-")}</strong></div>
                    <div><p>部门</p><strong>${escapeHtml(findDepartmentName(emp.deptId))}</strong></div>
                    <div><p>入职日期</p><strong>${formatDate(emp.entryDate)}</strong></div>
                </div>
            </div>
            ${exprHtml}
        `;
        elements.detailModal.classList.remove("hidden");
        setStatus("员工详情加载完成");
    } catch (error) {
        handleError(error);
    }
};

window.editEmployee = async function editEmployee(id) {
    try {
        setStatus("正在加载员工信息...");
        const emp = await apiRequest(`/emps/${id}`);
        openEmpModal(emp);
        setStatus("员工信息已载入");
    } catch (error) {
        handleError(error);
    }
};

window.deleteEmployee = async function deleteEmployee(id) {
    if (!window.confirm("确认删除这名员工吗？")) {
        return;
    }

    try {
        setStatus("正在删除员工...");
        await apiRequest(`/emps?ids=${id}`, { method: "DELETE" });
        await loadEmployees();
        await renderDashboard();
        setStatus("员工已删除");
    } catch (error) {
        handleError(error);
    }
};

async function batchDeleteEmployees() {
    const ids = Array.from(state.selectedEmployeeIds);
    if (!ids.length) {
        window.alert("请先选择需要删除的员工");
        return;
    }

    if (!window.confirm(`确认删除选中的 ${ids.length} 名员工吗？`)) {
        return;
    }

    try {
        setStatus("正在批量删除员工...");
        const query = ids.map((id) => `ids=${id}`).join("&");
        await apiRequest(`/emps?${query}`, { method: "DELETE" });
        await loadEmployees();
        await renderDashboard();
        setStatus("批量删除完成");
    } catch (error) {
        handleError(error);
    }
}

function findDepartmentName(deptId) {
    return state.departments.find((dept) => Number(dept.id) === Number(deptId))?.name || "未分配";
}

function setStatus(text) {
    elements.statusPill.textContent = text;
}

function handleError(error) {
    console.error(error);
    setStatus("操作失败");
    window.alert(error.message || "请求失败，请检查后端服务和数据库连接");
}

function genderText(gender) {
    return Number(gender) === 1 ? "男" : Number(gender) === 2 ? "女" : "-";
}

function formatDate(dateString) {
    return dateString ? String(dateString).slice(0, 10) : "-";
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) {
        return "-";
    }
    return String(dateTimeString).replace("T", " ").slice(0, 19);
}

function normalizeDateInput(value) {
    return value ? String(value).slice(0, 10) : "";
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#39;");
}
