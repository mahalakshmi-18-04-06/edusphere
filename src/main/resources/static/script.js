/* =========================================================
   EDUSPHERE - STUDENT PROFILE
   ========================================================= */

const token = localStorage.getItem("edusphere_token");


/* =========================================================
   AUTHENTICATION CHECK
   ========================================================= */

if (!token) {

    window.location.href = "/login.html";

}


/* =========================================================
   LOAD CURRENT STUDENT PROFILE
   ========================================================= */

async function loadStudentProfile() {

    try {

        const response = await fetch(
            "/api/auth/me",
            {
                method: "GET",

                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );


        /* ---------------------------------------------
           TOKEN EXPIRED / INVALID
           --------------------------------------------- */

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
                "Unable to load student profile."
            );

        }


        const data = await response.json();


        console.log(
            "EduSphere profile:",
            data
        );


        renderStudentProfile(data);

    }
    catch (error) {

        console.error(
            "Profile loading error:",
            error
        );

        showPageError(
            "Unable to load your profile. Please refresh the page."
        );

    }

}


/* =========================================================
   RENDER COMPLETE PROFILE
   ========================================================= */

function renderStudentProfile(data) {

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


    /* =====================================================
       BASIC STUDENT INFORMATION
       ===================================================== */

    const studentName =
        student.name || "Student";


    const studentEmail =
        student.email || "No email available";


    const department =
        student.department ||
        "Department not specified";


    const year =
        student.year
        ? "Year " + student.year
        : "";


    const section =
        student.section
        ? "Section " + student.section
        : "";


    /* =====================================================
       HERO
       ===================================================== */

    setText(
        "studentName",
        studentName
    );


    setText(
        "studentDepartment",
        buildAcademicTitle(
            department,
            year,
            section
        )
    );


    setText(
        "studentEmail",
        studentEmail
    );


    /* =====================================================
       ABOUT
       ===================================================== */

    setText(
        "aboutStudentName",
        studentName
    );


    setText(
        "aboutStudentEmail",
        studentEmail
    );


    setText(
        "registrationNumber",
        student.registrationNumber || "-"
    );


    setText(
        "department",
        department
    );


    setText(
        "year",
        student.year
            ? student.year
            : "-"
    );


    setText(
        "section",
        student.section || "-"
    );


    /* =====================================================
       SOCIAL LINKS
       ===================================================== */

    setupLink(
        "githubLink",
        profile.githubUrl
    );


    setupLink(
        "linkedinLink",
        profile.linkedinUrl
    );


    setupLink(
        "leetcodeLink",
        profile.leetcodeUrl
    );


    setupLink(
        "codechefLink",
        profile.codechefUrl
    );


    /* =====================================================
       PROFILE IMAGE
       ===================================================== */

    setupProfileImage(
        profile.profileImageUrl
    );


    /* =====================================================
       STATISTICS
       ===================================================== */

    setText(
        "projectCount",
        projects.length
    );


    setText(
        "skillCount",
        skills.length
    );


    setText(
        "certificationCount",
        certifications.length
    );


    setText(
        "achievementCount",
        achievements.length
    );


    /* =====================================================
       SKILLS
       ===================================================== */

    renderSkills(skills);


    /* =====================================================
       PROJECTS
       ===================================================== */

    renderProjects(projects);


    /* =====================================================
       CERTIFICATIONS
       ===================================================== */

    renderCertifications(
        certifications
    );


    /* =====================================================
       ACHIEVEMENTS
       ===================================================== */

    renderAchievements(
        achievements
    );

}


/* =========================================================
   BUILD ACADEMIC TITLE
   ========================================================= */

function buildAcademicTitle(
    department,
    year,
    section
) {

    const parts = [];

    if (department) {
        parts.push(department);
    }

    if (year) {
        parts.push(year);
    }

    if (section) {
        parts.push(section);
    }

    return parts.length > 0
        ? parts.join(" • ")
        : "Student";
}


/* =========================================================
   SET TEXT SAFELY
   ========================================================= */

function setText(
    elementId,
    value
) {

    const element =
        document.getElementById(
            elementId
        );


    if (element) {

        element.textContent =
            value ?? "";

    }

}


/* =========================================================
   SET LINK
   ========================================================= */

function setupLink(
    elementId,
    url
) {

    const element =
        document.getElementById(
            elementId
        );


    if (!element) {
        return;
    }


    if (url && url.trim() !== "") {

        element.href = url;

        element.target = "_blank";

        element.rel =
            "noopener noreferrer";

        element.style.display =
            "inline-flex";

    }
    else {

        element.removeAttribute("href");

        element.style.display =
            "none";

    }

}


/* =========================================================
   PROFILE IMAGE
   ========================================================= */

function setupProfileImage(
    imageUrl
) {

    const image =
        document.getElementById(
            "profileImage"
        );


    if (!image) {
        return;
    }


    /*
       If the student has a profile image URL,
       display it.
    */

    if (
        imageUrl &&
        imageUrl.trim() !== ""
    ) {

        image.src = imageUrl;

        image.alt = "Student Profile";

        image.style.display =
            "block";


        /*
           If the URL itself is broken,
           hide the broken image.
        */

        image.onerror = function () {

            image.style.display =
                "none";

        };

    }
    else {

        /*
           No profile image URL:
           hide the image completely.
        */

        image.removeAttribute("src");

        image.style.display =
            "none";

    }

}


/* =========================================================
   RENDER SKILLS
   ========================================================= */

function renderSkills(
    skills
) {

    const container =
        document.getElementById(
            "skillsContainer"
        );


    if (!container) {
        return;
    }


    if (
        !skills ||
        skills.length === 0
    ) {

        container.innerHTML = `
            <div class="empty-message">
                No skills added yet.
            </div>
        `;

        return;
    }


    container.innerHTML =
        skills.map(skill => `

            <div class="skill-card">

                <h3>
                    ${escapeHTML(
                        skill.name || "Skill"
                    )}
                </h3>

                ${
                    skill.category
                    ?
                    `
                    <p>
                        ${escapeHTML(
                            skill.category
                        )}
                    </p>
                    `
                    :
                    ""
                }

                ${
                    skill.proficiency
                    ?
                    `
                    <span class="skill-badge">
                        ${escapeHTML(
                            skill.proficiency
                        )}
                    </span>
                    `
                    :
                    ""
                }

            </div>

        `).join("");

}


/* =========================================================
   RENDER PROJECTS
   ========================================================= */

function renderProjects(
    projects
) {

    const container =
        document.getElementById(
            "projectsContainer"
        );


    if (!container) {
        return;
    }


    if (
        !projects ||
        projects.length === 0
    ) {

        container.innerHTML = `
            <div class="empty-message">
                No projects added yet.
            </div>
        `;

        return;
    }


    container.innerHTML =
        projects.map(project => `

            <div class="project-card">

                <h3>
                    ${escapeHTML(
                        project.title ||
                        "Untitled Project"
                    )}
                </h3>


                <p>
                    ${escapeHTML(
                        project.description ||
                        "No description available."
                    )}
                </p>


                ${
                    project.technologies
                    ?
                    `
                    <p>
                        <strong>
                            Technologies:
                        </strong>

                        ${escapeHTML(
                            project.technologies
                        )}
                    </p>
                    `
                    :
                    ""
                }


                <div>

                    ${
                        project.githubUrl
                        ?
                        `
                        <a
                            href="${escapeAttribute(
                                project.githubUrl
                            )}"
                            target="_blank"
                            rel="noopener noreferrer"
                        >
                            GitHub
                        </a>
                        `
                        :
                        ""
                    }


                    ${
                        project.liveDemoUrl
                        ?
                        `
                        <a
                            href="${escapeAttribute(
                                project.liveDemoUrl
                            )}"
                            target="_blank"
                            rel="noopener noreferrer"
                        >
                            Live Demo
                        </a>
                        `
                        :
                        ""
                    }

                </div>

            </div>

        `).join("");

}


/* =========================================================
   RENDER CERTIFICATIONS
   ========================================================= */

function renderCertifications(certifications) {

    const container =
        document.getElementById("certificationsContainer");

    if (!container) {
        return;
    }

    if (!certifications || certifications.length === 0) {

        container.innerHTML = `
            <div class="empty-message">
                No certifications added yet.
            </div>
        `;

        return;
    }

    container.innerHTML = certifications.map(certification => `

        <div class="certification-card">

            <h3>
                ${escapeHTML(
                    certification.title || "Certification"
                )}
            </h3>

            ${
                certification.issuingOrganization
                ? `
                    <p>
                        ${escapeHTML(
                            certification.issuingOrganization
                        )}
                    </p>
                  `
                : ""
            }

            ${
                certification.issueDate
                ? `
                    <p>
                        Issued:
                        ${escapeHTML(
                            certification.issueDate
                        )}
                    </p>
                  `
                : ""
            }

            ${
                certification.credentialUrl
                ? `
                    <a
                        href="${escapeAttribute(
                            certification.credentialUrl
                        )}"
                        target="_blank"
                        rel="noopener noreferrer"
                    >
                        View Certificate →
                    </a>
                  `
                : ""
            }

        </div>

    `).join("");
}


/* =========================================================
   RENDER ACHIEVEMENTS
   ========================================================= */

   function renderAchievements(achievements) {

    const container =
        document.getElementById("achievementsContainer");

    if (!container) {
        return;
    }

    if (!achievements || achievements.length === 0) {

        container.innerHTML = `
            <div class="empty-message">
                No achievements added yet.
            </div>
        `;

        return;
    }

    container.innerHTML = achievements.map(achievement => `

        <div class="achievement-card">

            <h3>
                ${escapeHTML(
                    achievement.title || "Achievement"
                )}
            </h3>

            ${
                achievement.description
                ? `
                    <p>
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
                    <p>
                        <strong>Type:</strong>
                        ${escapeHTML(
                            achievement.achievementType
                        )}
                    </p>
                  `
                : ""
            }

            ${
                achievement.achievementDate
                ? `
                    <p>
                        Date:
                        ${escapeHTML(
                            achievement.achievementDate
                        )}
                    </p>
                  `
                : ""
            }

            ${
                achievement.proofUrl
                ? `
                    <a
                        href="${escapeAttribute(
                            achievement.proofUrl
                        )}"
                        target="_blank"
                        rel="noopener noreferrer"
                    >
                        View Proof →
                    </a>
                  `
                : ""
            }

        </div>

    `).join("");
}



/* =========================================================
   HTML SECURITY
   ========================================================= */

function escapeHTML(
    value
) {

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");

}


function escapeAttribute(
    value
) {

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/"/g, "&quot;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");

}


/* =========================================================
   ERROR MESSAGE
   ========================================================= */

function showPageError(
    message
) {

    console.error(message);


    const containers = [
        "skillsContainer",
        "projectsContainer",
        "certificationsContainer",
        "achievementsContainer"
    ];


    containers.forEach(id => {

        const element =
            document.getElementById(id);

        if (element) {

            element.innerHTML = `
                <div class="empty-message">
                    ${escapeHTML(message)}
                </div>
            `;

        }

    });

}


/* =========================================================
   LOGOUT
   ========================================================= */

function logout() {

    localStorage.removeItem(
        "edusphere_token"
    );

    window.location.href =
        "/login.html";

}


/* =========================================================
   START
   ========================================================= */

loadStudentProfile();