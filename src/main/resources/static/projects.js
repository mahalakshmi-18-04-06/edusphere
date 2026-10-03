const token = localStorage.getItem("edusphere_token");

if (!token) {
    window.location.href = "/login.html";
}


/* =========================
   LOAD PROJECTS
========================= */

async function loadProjects() {

    const container =
        document.getElementById("projectsContainer");

    try {

        const response = await fetch(
            "/api/projects/me",
            {
                headers: {
                    "Authorization":
                        "Bearer " + token
                }
            }
        );

        if (response.status === 401 ||
            response.status === 403) {

            localStorage.removeItem(
                "edusphere_token"
            );

            window.location.href =
                "/login.html";

            return;
        }

        if (!response.ok) {
            throw new Error(
                "Failed to load projects"
            );
        }

        const projects =
            await response.json();

        renderProjects(projects);

    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <p class="empty-message">
                Unable to load projects.
            </p>
        `;
    }
}


/* =========================
   RENDER PROJECTS
========================= */

function renderProjects(projects) {

    const container =
        document.getElementById(
            "projectsContainer"
        );

    if (!projects ||
        projects.length === 0) {

        container.innerHTML = `
            <div class="empty-message">

                <h3>
                    No projects yet
                </h3>

                <p>
                    Add your first project
                    to showcase your work.
                </p>

            </div>
        `;

        return;
    }

    container.innerHTML =
        projects.map(project => {

            const technologies =
                project.technologies
                    ? project.technologies
                        .split(",")
                        .map(
                            tech =>
                                `<span class="tech-tag">
                                    ${escapeHTML(
                                        tech.trim()
                                    )}
                                </span>`
                        )
                        .join("")
                    : "";

            const github =
                project.githubUrl
                    ? `
                        <a
                            href="${escapeAttribute(
                                project.githubUrl
                            )}"
                            target="_blank"
                            rel="noopener noreferrer"
                            class="project-link"
                        >
                            GitHub
                        </a>
                    `
                    : "";

            const liveDemo =
                project.liveDemoUrl
                    ? `
                        <a
                            href="${escapeAttribute(
                                project.liveDemoUrl
                            )}"
                            target="_blank"
                            rel="noopener noreferrer"
                            class="project-link"
                        >
                            Live Demo
                        </a>
                    `
                    : "";

            return `

                <article
                    class="project-card"
                >

                    <h2 class="project-title">
                        ${escapeHTML(
                            project.title || ""
                        )}
                    </h2>


                    <p class="project-description">
                        ${escapeHTML(
                            project.description || ""
                        )}
                    </p>


                    <div class="project-tech">

                        ${technologies}

                    </div>


                    <div class="project-links">

                        ${github}

                        ${liveDemo}

                    </div>


                    <div class="project-actions">

                        <button
                            class="edit-btn"
                            onclick="editProject(
                                ${project.id}
                            )"
                        >
                            Edit
                        </button>


                        <button
                            class="delete-btn"
                            onclick="deleteProject(
                                ${project.id}
                            )"
                        >
                            Delete
                        </button>

                    </div>

                </article>

            `;

        }).join("");
}


/* =========================
   OPEN ADD PROJECT
========================= */

function openAddProject() {

    document.getElementById(
        "modalTitle"
    ).textContent = "Add Project";

    document.getElementById(
        "projectForm"
    ).reset();

    document.getElementById(
        "projectId"
    ).value = "";

    document.getElementById(
        "projectModal"
    ).classList.add("show");
}


/* =========================
   CLOSE MODAL
========================= */

function closeProjectModal() {

    document.getElementById(
        "projectModal"
    ).classList.remove("show");
}


/* =========================
   EDIT PROJECT
========================= */

async function editProject(id) {

    try {

        const response = await fetch(
            "/api/projects/me",
            {
                headers: {
                    "Authorization":
                        "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error(
                "Unable to load project"
            );
        }

        const projects =
            await response.json();

        const project =
            projects.find(
                item => item.id === id
            );

        if (!project) {
            showMessage(
                "Project not found.",
                "error"
            );

            return;
        }

        document.getElementById(
            "modalTitle"
        ).textContent = "Edit Project";

        document.getElementById(
            "projectId"
        ).value = project.id;

        document.getElementById(
            "projectTitle"
        ).value =
            project.title || "";

        document.getElementById(
            "projectDescription"
        ).value =
            project.description || "";

        document.getElementById(
            "projectTechnologies"
        ).value =
            project.technologies || "";

        document.getElementById(
            "githubUrl"
        ).value =
            project.githubUrl || "";

        document.getElementById(
            "liveDemoUrl"
        ).value =
            project.liveDemoUrl || "";

        document.getElementById(
            "projectModal"
        ).classList.add("show");

    } catch (error) {

        console.error(error);

        showMessage(
            "Unable to open project.",
            "error"
        );
    }
}


/* =========================
   SAVE PROJECT
========================= */

document.getElementById(
    "projectForm"
).addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();

        const id =
            document.getElementById(
                "projectId"
            ).value;

        const projectData = {

            title:
                document.getElementById(
                    "projectTitle"
                ).value.trim(),

            description:
                document.getElementById(
                    "projectDescription"
                ).value.trim(),

            technologies:
                document.getElementById(
                    "projectTechnologies"
                ).value.trim(),

            githubUrl:
                document.getElementById(
                    "githubUrl"
                ).value.trim(),

            liveDemoUrl:
                document.getElementById(
                    "liveDemoUrl"
                ).value.trim()
        };

        const url =
            id
                ? `/api/projects/${id}`
                : "/api/projects";

        const method =
            id ? "PUT" : "POST";

        try {

            const response =
                await fetch(
                    url,
                    {
                        method: method,

                        headers: {
                            "Content-Type":
                                "application/json",

                            "Authorization":
                                "Bearer " + token
                        },

                        body:
                            JSON.stringify(
                                projectData
                            )
                    }
                );

            if (response.status === 401 ||
                response.status === 403) {

                localStorage.removeItem(
                    "edusphere_token"
                );

                window.location.href =
                    "/login.html";

                return;
            }

            if (!response.ok) {

                const errorText =
                    await response.text();

                throw new Error(
                    errorText ||
                    "Unable to save project"
                );
            }

            closeProjectModal();

            showMessage(
                id
                    ? "Project updated successfully."
                    : "Project added successfully.",
                "success"
            );

            await loadProjects();

        } catch (error) {

            console.error(error);

            showMessage(
                error.message ||
                "Unable to save project.",
                "error"
            );
        }
    }
);


/* =========================
   DELETE PROJECT
========================= */

async function deleteProject(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this project?"
        );

    if (!confirmed) {
        return;
    }

    try {

        const response =
            await fetch(
                `/api/projects/${id}`,
                {
                    method: "DELETE",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );

        if (response.status === 401 ||
            response.status === 403) {

            localStorage.removeItem(
                "edusphere_token"
            );

            window.location.href =
                "/login.html";

            return;
        }

        if (!response.ok) {

            throw new Error(
                "Unable to delete project"
            );
        }

        showMessage(
            "Project deleted successfully.",
            "success"
        );

        await loadProjects();

    } catch (error) {

        console.error(error);

        showMessage(
            "Unable to delete project.",
            "error"
        );
    }
}


/* =========================
   MESSAGE
========================= */

function showMessage(
    message,
    type
) {

    const element =
        document.getElementById(
            "message"
        );

    element.innerHTML = `
        <div
            class="${type}-message"
        >
            ${escapeHTML(message)}
        </div>
    `;

    setTimeout(() => {

        element.innerHTML = "";

    }, 3000);
}


/* =========================
   SECURITY HELPERS
========================= */

function escapeHTML(value) {

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}


function escapeAttribute(value) {

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll('"', "&quot;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;");
}


/* =========================
   CLOSE MODAL ON BACKDROP
========================= */

document.getElementById(
    "projectModal"
).addEventListener(
    "click",
    function (event) {

        if (event.target === this) {
            closeProjectModal();
        }

    }
);


/* =========================
   INITIAL LOAD
========================= */

loadProjects();