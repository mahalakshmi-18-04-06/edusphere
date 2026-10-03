const token =
    localStorage.getItem("edusphere_token");

if (!token) {
    window.location.href = "/login.html";
}


let allStudents = [];


/* =========================================
   LOAD ADMIN DASHBOARD
========================================= */

async function loadAdminDashboard() {

    try {

        const response =
            await fetch(
                "/api/admin/dashboard",
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );


        if (
            response.status === 401 ||
            response.status === 403
        ) {

            localStorage.removeItem(
                "edusphere_token"
            );

            window.location.href =
                "/login.html";

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Failed to load admin dashboard"
            );
        }


        const data =
            await response.json();


        /* ==============================
           STATISTICS
        ============================== */

        document.getElementById(
            "totalStudents"
        ).textContent =
            data.totalStudents ?? 0;


        document.getElementById(
            "activeStudents"
        ).textContent =
            data.activeStudents ?? 0;


        document.getElementById(
            "inactiveStudents"
        ).textContent =
            data.inactiveStudents ?? 0;


        const departmentCounts =
            data.departmentCounts || {};


        const totalDepartments =
            Object.keys(
                departmentCounts
            ).length;


        document.getElementById(
            "totalDepartments"
        ).textContent =
            totalDepartments;


        /* ==============================
           ANALYTICS
        ============================== */

        renderAnalytics(
            "departmentAnalytics",
            departmentCounts
        );


        renderAnalytics(
            "yearAnalytics",
            data.yearCounts || {}
        );


    } catch (error) {

        console.error(
            "Admin Dashboard Error:",
            error
        );


        document.getElementById(
            "departmentAnalytics"
        ).innerHTML =
            `
            <p class="analytics-loading">
                Unable to load analytics.
            </p>
            `;


        document.getElementById(
            "yearAnalytics"
        ).innerHTML =
            `
            <p class="analytics-loading">
                Unable to load analytics.
            </p>
            `;
    }
}


/* =========================================
   RENDER ANALYTICS
========================================= */

function renderAnalytics(
    containerId,
    data
) {

    const container =
        document.getElementById(
            containerId
        );


    if (!container) {
        return;
    }


    container.innerHTML = "";


    const entries =
        Object.entries(data);


    if (entries.length === 0) {

        container.innerHTML =
            `
            <p class="analytics-loading">
                No data available.
            </p>
            `;

        return;
    }


    const maxValue =
        Math.max(
            ...entries.map(
                ([, value]) =>
                    Number(value)
            )
        );


    entries.forEach(
        ([label, value]) => {

            const numericValue =
                Number(value);


            const percentage =
                maxValue > 0
                    ? (numericValue / maxValue) * 100
                    : 0;


            const row =
                document.createElement(
                    "div"
                );


            row.className =
                "analytics-row";


            row.innerHTML = `

                <div class="analytics-row-header">

                    <span class="analytics-label">
                        ${escapeHTML(label)}
                    </span>

                    <span class="analytics-value">
                        ${numericValue}
                    </span>

                </div>


                <div class="analytics-bar">

                    <div
                        class="analytics-bar-fill"
                        style="width: ${percentage}%">
                    </div>

                </div>

            `;


            container.appendChild(
                row
            );

        }
    );
}


/* =========================================
   LOAD STUDENTS
========================================= */

async function loadStudents() {

    try {

        const response =
            await fetch(
                "/api/admin/students",
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );


        if (
            response.status === 401 ||
            response.status === 403
        ) {

            localStorage.removeItem(
                "edusphere_token"
            );

            window.location.href =
                "/login.html";

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Failed to load students"
            );
        }


        allStudents =
            await response.json();


        renderStudents(
            allStudents
        );


    } catch (error) {

        console.error(
            "Student Loading Error:",
            error
        );


        const errorMessage =
            document.getElementById(
                "errorMessage"
            );


        if (errorMessage) {

            errorMessage.textContent =
                "Unable to load students.";
        }
    }
}


/* =========================================
   RENDER STUDENTS
========================================= */

function renderStudents(
    students
) {

    const tableBody =
        document.getElementById(
            "studentTableBody"
        );


    if (!tableBody) {
        return;
    }


    tableBody.innerHTML = "";


    if (
        !students ||
        students.length === 0
    ) {

        tableBody.innerHTML = `

            <tr>

                <td
                    colspan="8"
                    class="loading">

                    No students found.

                </td>

            </tr>

        `;

        return;
    }


    students.forEach(
        student => {

            const row =
                document.createElement(
                    "tr"
                );


            const isActive =
                Boolean(
                    student.active
                );


            row.innerHTML = `

                <td>
                    ${escapeHTML(
                        student.registrationNumber
                        || "-"
                    )}
                </td>


                <td>
                    ${escapeHTML(
                        student.name
                        || "-"
                    )}
                </td>


                <td>
                    ${escapeHTML(
                        student.email
                        || "-"
                    )}
                </td>


                <td>
                    ${escapeHTML(
                        student.department
                        || "-"
                    )}
                </td>


                <td>
                    ${student.year ?? "-"}
                </td>


                <td>
                    ${escapeHTML(
                        student.section
                        || "-"
                    )}
                </td>


                <td>

                    <span class="${
                        isActive
                            ? "status active"
                            : "status inactive"
                    }">

                        ${
                            isActive
                                ? "Active"
                                : "Inactive"
                        }

                    </span>

                </td>


                <td>

                    <button
                        class="view-btn"
                        onclick="viewProfile(${student.id})">

                        View

                    </button>


                    ${
                        isActive

                            ? `

                                <button
                                    class="action-btn danger"
                                    onclick="deactivateStudent(${student.id})">

                                    Deactivate

                                </button>

                              `

                            : `

                                <button
                                    class="action-btn success"
                                    onclick="activateStudent(${student.id})">

                                    Activate

                                </button>

                              `
                    }

                </td>

            `;


            tableBody.appendChild(
                row
            );

        }
    );
}


/* =========================================
   SEARCH STUDENTS
========================================= */

function searchStudents() {

    const input =
        document.getElementById(
            "searchInput"
        );


    if (!input) {
        return;
    }


    const searchValue =
        input.value
            .toLowerCase()
            .trim();


    if (!searchValue) {

        renderStudents(
            allStudents
        );

        return;
    }


    const filteredStudents =
        allStudents.filter(
            student => {

                return (

                    String(
                        student.registrationNumber
                        || ""
                    )
                        .toLowerCase()
                        .includes(
                            searchValue
                        )

                    ||

                    String(
                        student.name
                        || ""
                    )
                        .toLowerCase()
                        .includes(
                            searchValue
                        )

                    ||

                    String(
                        student.email
                        || ""
                    )
                        .toLowerCase()
                        .includes(
                            searchValue
                        )

                    ||

                    String(
                        student.department
                        || ""
                    )
                        .toLowerCase()
                        .includes(
                            searchValue
                        )

                );

            }
        );


    renderStudents(
        filteredStudents
    );
}


/* =========================================
   VIEW PROFILE
========================================= */

async function viewProfile(id) {

    const modal =
        document.getElementById(
            "profileModal"
        );


    const content =
        document.getElementById(
            "profileContent"
        );


    if (!modal || !content) {
        return;
    }


    /* Open modal immediately */

    modal.classList.add(
        "show"
    );


    content.innerHTML = `

        <div class="profile-modal-body">

            <div class="profile-empty">

                Loading student profile...

            </div>

        </div>

    `;


    try {

        const response =
            await fetch(
                `/api/admin/students/${id}`,
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " +
                            localStorage.getItem(
                                "edusphere_token"
                            )
                    }
                }
            );


        if (
            response.status === 401 ||
            response.status === 403
        ) {

            localStorage.removeItem(
                "edusphere_token"
            );

            window.location.href =
                "/login.html";

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Unable to load student profile"
            );
        }


        const data =
            await response.json();


        const student =
            data.student || {};


        const profile =
            data.profile || {};


        const projects =
            data.projects || [];


        const skills =
            data.skills || [];


        const certifications =
            data.certifications || [];


        const achievements =
            data.achievements || [];


        /* =====================================
           PROFESSIONAL LINKS
        ===================================== */

        const profileLinks = [

            [
                "GitHub",
                profile.githubUrl
            ],

            [
                "LinkedIn",
                profile.linkedinUrl
            ],

            [
                "Portfolio",
                profile.portfolioUrl
            ],

            [
                "Resume",
                profile.resumeUrl
            ],

            [
                "LeetCode",
                profile.leetcodeUrl
            ],

            [
                "CodeChef",
                profile.codechefUrl
            ]

        ];


        const linksHTML =
            profileLinks
                .map(
                    ([name, url]) => {

                        if (url) {

                            return `

                                <a
                                    href="${escapeAttribute(url)}"
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    class="profile-link">

                                    ${name}

                                </a>

                            `;

                        }


                        return `

                            <span
                                class="profile-link disabled">

                                ${name}

                            </span>

                        `;

                    }
                )
                .join("");


        /* =====================================
           PROFILE HEADER
        ===================================== */

        let html = `

            <div class="profile-modal-header">

                <p class="profile-eyebrow">
                    STUDENT PROFILE
                </p>


                <h2>
                    ${escapeHTML(
                        student.name ||
                        "Student"
                    )}
                </h2>


                <p class="profile-registration">

                    ${escapeHTML(
                        student.registrationNumber ||
                        "-"
                    )}

                </p>

            </div>


            <div class="profile-modal-body">


                <!-- BASIC INFORMATION -->

                <section class="profile-section">

                    <h3 class="profile-section-title">
                        Basic Information
                    </h3>


                    <div class="profile-info-grid">


                        <div class="profile-info-item">

                            <span class="profile-info-label">
                                Name
                            </span>

                            <span class="profile-info-value">
                                ${escapeHTML(
                                    student.name ||
                                    "-"
                                )}
                            </span>

                        </div>


                        <div class="profile-info-item">

                            <span class="profile-info-label">
                                Email
                            </span>

                            <span class="profile-info-value">
                                ${escapeHTML(
                                    student.email ||
                                    "-"
                                )}
                            </span>

                        </div>


                        <div class="profile-info-item">

                            <span class="profile-info-label">
                                Department
                            </span>

                            <span class="profile-info-value">
                                ${escapeHTML(
                                    student.department ||
                                    "-"
                                )}
                            </span>

                        </div>


                        <div class="profile-info-item">

                            <span class="profile-info-label">
                                Year
                            </span>

                            <span class="profile-info-value">
                                ${
                                    student.year ??
                                    "-"
                                }
                            </span>

                        </div>


                        <div class="profile-info-item">

                            <span class="profile-info-label">
                                Section
                            </span>

                            <span class="profile-info-value">
                                ${escapeHTML(
                                    student.section ||
                                    "-"
                                )}
                            </span>

                        </div>


                        <div class="profile-info-item">

                            <span class="profile-info-label">
                                Account Status
                            </span>

                            <span class="profile-info-value">

                                ${
                                    student.active
                                        ? "Active"
                                        : "Inactive"
                                }

                            </span>

                        </div>

                    </div>

                </section>


                <!-- PROFESSIONAL PROFILES -->

                <section class="profile-section">

                    <h3 class="profile-section-title">

                        Professional Profiles

                    </h3>


                    <div class="profile-links">

                        ${linksHTML}

                    </div>

                </section>


                <!-- PROJECTS -->

                <section class="profile-section">

                    <h3 class="profile-section-title">

                        Projects
                        (${projects.length})

                    </h3>

        `;


        if (projects.length === 0) {

            html += `

                <div class="profile-empty">

                    No projects added.

                </div>

            `;

        } else {

            projects.forEach(
                project => {

                    html += `

                        <div class="profile-item-card">

                            <h4 class="profile-item-title">

                                ${escapeHTML(
                                    project.title ||
                                    "-"
                                )}

                            </h4>


                            <p class="profile-item-description">

                                ${escapeHTML(
                                    project.description ||
                                    "No description available."
                                )}

                            </p>


                            ${
                                project.technologies
                                    ? `

                                        <div
                                            class="profile-item-meta">

                                            <strong>
                                                Technologies:
                                            </strong>

                                            ${escapeHTML(
                                                project.technologies
                                            )}

                                        </div>

                                      `
                                    : ""
                            }


                            <div
                                class="profile-item-links">


                                ${
                                    project.githubUrl
                                        ? `

                                            <a
                                                href="${escapeAttribute(
                                                    project.githubUrl
                                                )}"
                                                target="_blank"
                                                rel="noopener noreferrer">

                                                GitHub →

                                            </a>

                                          `
                                        : ""
                                }


                                ${
                                    project.liveDemoUrl
                                        ? `

                                            <a
                                                href="${escapeAttribute(
                                                    project.liveDemoUrl
                                                )}"
                                                target="_blank"
                                                rel="noopener noreferrer">

                                                Live Demo →

                                            </a>

                                          `
                                        : ""
                                }


                            </div>

                        </div>

                    `;

                }
            );

        }


        html += `

                </section>


                <!-- SKILLS -->

                <section class="profile-section">

                    <h3 class="profile-section-title">

                        Skills
                        (${skills.length})

                    </h3>


                    <div class="profile-skills">

        `;


        if (skills.length === 0) {

            html += `

                <div class="profile-empty">

                    No skills added.

                </div>

            `;

        } else {

            skills.forEach(
                skill => {

                    html += `

                        <div class="profile-skill">

                            <span>

                                ${escapeHTML(
                                    skill.name ||
                                    "-"
                                )}

                            </span>


                            ${
                                skill.proficiency
                                    ? `

                                        <span
                                            class="profile-skill-level">

                                            ${escapeHTML(
                                                skill.proficiency
                                            )}

                                        </span>

                                      `
                                    : ""
                            }

                        </div>

                    `;

                }
            );

        }


        html += `

                    </div>

                </section>


                <!-- CERTIFICATIONS -->

                <section class="profile-section">

                    <h3 class="profile-section-title">

                        Certifications
                        (${certifications.length})

                    </h3>

        `;


        if (certifications.length === 0) {

            html += `

                <div class="profile-empty">

                    No certifications added.

                </div>

            `;

        } else {

            certifications.forEach(
                certification => {

                    /*
                     * Supports both possible
                     * field naming styles.
                     */

                    const certName =
                        certification.name ||
                        certification.title ||
                        "Certification";


                    const issuer =
                        certification.issuer ||
                        certification.issuingOrganization ||
                        "";


                    const issueDate =
                        certification.issueDate ||
                        "";


                    html += `

                        <div class="profile-item-card">

                            <h4 class="profile-item-title">

                                ${escapeHTML(
                                    certName
                                )}

                            </h4>


                            ${
                                issuer
                                    ? `

                                        <div
                                            class="profile-item-meta">

                                            <strong>
                                                Issuer:
                                            </strong>

                                            ${escapeHTML(
                                                issuer
                                            )}

                                        </div>

                                      `
                                    : ""
                            }


                            ${
                                issueDate
                                    ? `

                                        <div
                                            class="profile-item-meta">

                                            <strong>
                                                Issued:
                                            </strong>

                                            ${escapeHTML(
                                                issueDate
                                            )}

                                        </div>

                                      `
                                    : ""
                            }


                            ${
                                certification.credentialUrl
                                    ? `

                                        <div
                                            class="profile-item-links">

                                            <a
                                                href="${escapeAttribute(
                                                    certification.credentialUrl
                                                )}"
                                                target="_blank"
                                                rel="noopener noreferrer">

                                                View Credential →

                                            </a>

                                        </div>

                                      `
                                    : ""
                            }

                        </div>

                    `;

                }
            );

        }


        html += `

                </section>


                <!-- ACHIEVEMENTS -->

                <section class="profile-section">

                    <h3 class="profile-section-title">

                        Achievements
                        (${achievements.length})

                    </h3>

        `;


        if (achievements.length === 0) {

            html += `

                <div class="profile-empty">

                    No achievements added.

                </div>

            `;

        } else {

            achievements.forEach(
                achievement => {

                    const achievementTitle =
                        achievement.title ||
                        achievement.name ||
                        "Achievement";


                    const achievementDate =
                        achievement.achievementDate ||
                        "";


                    html += `

                        <div class="profile-item-card">

                            <h4 class="profile-item-title">

                                ${escapeHTML(
                                    achievementTitle
                                )}

                            </h4>


                            ${
                                achievement.description
                                    ? `

                                        <p
                                            class="profile-item-description">

                                            ${escapeHTML(
                                                achievement.description
                                            )}

                                        </p>

                                      `
                                    : ""
                            }


                            ${
                                achievement.achievementType
                                    ? `

                                        <div
                                            class="profile-item-meta">

                                            <strong>
                                                Type:
                                            </strong>

                                            ${escapeHTML(
                                                achievement.achievementType
                                            )}

                                        </div>

                                      `
                                    : ""
                            }


                            ${
                                achievementDate
                                    ? `

                                        <div
                                            class="profile-item-meta">

                                            <strong>
                                                Date:
                                            </strong>

                                            ${escapeHTML(
                                                achievementDate
                                            )}

                                        </div>

                                      `
                                    : ""
                            }


                            ${
                                achievement.proofUrl
                                    ? `

                                        <div
                                            class="profile-item-links">

                                            <a
                                                href="${escapeAttribute(
                                                    achievement.proofUrl
                                                )}"
                                                target="_blank"
                                                rel="noopener noreferrer">

                                                View Proof →

                                            </a>

                                        </div>

                                      `
                                    : ""
                            }

                        </div>

                    `;

                }
            );

        }


        html += `

                </section>

            </div>

        `;


        content.innerHTML =
            html;


    } catch (error) {

        console.error(
            "Profile Error:",
            error
        );


        content.innerHTML = `

            <div class="profile-modal-body">

                <div class="profile-empty">

                    Unable to load student profile.

                </div>

            </div>

        `;
    }
}


/* =========================================
   CLOSE PROFILE
========================================= */

function closeProfile() {

    const modal =
        document.getElementById(
            "profileModal"
        );


    if (modal) {

        modal.classList.remove(
            "show"
        );
    }
}


/* =========================================
   CLOSE MODAL WHEN CLICKING OUTSIDE
========================================= */

document.addEventListener(
    "click",
    function (event) {

        const modal =
            document.getElementById(
                "profileModal"
            );


        if (
            modal &&
            event.target === modal
        ) {

            closeProfile();
        }

    }
);


/* =========================================
   DEACTIVATE STUDENT
========================================= */

async function deactivateStudent(
    studentId
) {

    if (
        !confirm(
            "Are you sure you want to deactivate this student?"
        )
    ) {

        return;
    }


    try {

        const response =
            await fetch(
                `/api/admin/students/${studentId}/deactivate`,
                {
                    method: "PUT",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );


        if (!response.ok) {

            throw new Error(
                "Failed to deactivate student"
            );
        }


        await loadStudents();

        await loadAdminDashboard();


    } catch (error) {

        console.error(
            "Deactivate Error:",
            error
        );

    }
}


/* =========================================
   ACTIVATE STUDENT
========================================= */

async function activateStudent(
    studentId
) {

    try {

        const response =
            await fetch(
                `/api/admin/students/${studentId}/activate`,
                {
                    method: "PUT",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );


        if (!response.ok) {

            throw new Error(
                "Failed to activate student"
            );
        }


        await loadStudents();

        await loadAdminDashboard();


    } catch (error) {

        console.error(
            "Activate Error:",
            error
        );

    }
}


/* =========================================
   LOGOUT
========================================= */

function logout() {

    localStorage.removeItem(
        "edusphere_token"
    );


    window.location.href =
        "/login.html";
}


/* =========================================
   HTML SAFETY
========================================= */

function escapeHTML(
    value
) {

    const div =
        document.createElement(
            "div"
        );


    div.textContent =
        value ?? "";


    return div.innerHTML;
}


/* =========================================
   ATTRIBUTE SAFETY
========================================= */

function escapeAttribute(
    value
) {

    return String(
        value ?? ""
    )
        .replace(
            /&/g,
            "&amp;"
        )
        .replace(
            /"/g,
            "&quot;"
        )
        .replace(
            /'/g,
            "&#39;"
        )
        .replace(
            /</g,
            "&lt;"
        )
        .replace(
            />/g,
            "&gt;"
        );
}


/* =========================================
   INITIAL LOAD
========================================= */

loadAdminDashboard();

loadStudents();