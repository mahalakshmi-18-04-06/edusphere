const token = localStorage.getItem("edusphere_token");

if (!token) {
    window.location.href = "/login.html";
}


/* =========================
   LOAD SKILLS
========================= */

async function loadSkills() {

    const container =
        document.getElementById("skillsContainer");

    try {

        const response = await fetch(
            "/api/skills/me",
            {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (response.status === 401 ||
            response.status === 403) {

            localStorage.removeItem("edusphere_token");
            window.location.href = "/login.html";
            return;
        }

        if (!response.ok) {
            throw new Error("Failed to load skills");
        }

        const skills = await response.json();

        renderSkills(skills);

    } catch (error) {

        console.error("Load Skills Error:", error);

        container.innerHTML = `
            <p class="error-text">
                Unable to load skills.
            </p>
        `;
    }
}


/* =========================
   RENDER SKILLS
========================= */

function renderSkills(skills) {

    const container =
        document.getElementById("skillsContainer");

    container.innerHTML = "";

    if (!skills || skills.length === 0) {

        container.innerHTML = `
            <div class="empty-state">

                <h3>No skills added yet</h3>

                <p>
                    Add your first technical skill
                    to build your career profile.
                </p>

            </div>
        `;

        return;
    }

    skills.forEach(skill => {

        const card =
            document.createElement("div");

        card.className = "skill-card";

        card.innerHTML = `

            <div class="skill-card-top">

                <div>

                    <h3>
                        ${escapeHTML(skill.name || "-")}
                    </h3>

                    ${
                        skill.category
                            ? `
                                <p class="skill-category">
                                    ${escapeHTML(skill.category)}
                                </p>
                            `
                            : ""
                    }

                </div>

                <span class="proficiency-badge">
                    ${escapeHTML(
                        skill.proficiency || "-"
                    )}
                </span>

            </div>

            <div class="skill-actions">

                <button
                    class="edit-btn"
                    onclick="openEditSkill(${skill.id})"
                >
                    Edit
                </button>

                <button
                    class="delete-btn"
                    onclick="deleteSkill(${skill.id})"
                >
                    Delete
                </button>

            </div>
        `;

        container.appendChild(card);

    });
}


/* =========================
   OPEN ADD SKILL
========================= */

function openAddSkill() {

    document.getElementById(
        "modalTitle"
    ).textContent = "Add Skill";

    document.getElementById(
        "skillId"
    ).value = "";

    document.getElementById(
        "skillName"
    ).value = "";

    document.getElementById(
        "proficiency"
    ).value = "";

    document.getElementById(
        "skillModal"
    ).classList.add("show");
}


/* =========================
   OPEN EDIT SKILL
========================= */

async function openEditSkill(id) {

    try {

        const response = await fetch(
            "/api/skills/me",
            {
                headers: {
                    "Authorization":
                        "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to load skills");
        }

        const skills = await response.json();

        const skill =
            skills.find(item => item.id === id);

        if (!skill) {
            showMessage(
                "Skill not found.",
                "error"
            );
            return;
        }

        document.getElementById(
            "modalTitle"
        ).textContent = "Edit Skill";

        document.getElementById(
            "skillId"
        ).value = skill.id;

        document.getElementById(
            "skillName"
        ).value = skill.name || "";

        document.getElementById(
            "proficiency"
        ).value = skill.proficiency || "";

        document.getElementById(
            "skillModal"
        ).classList.add("show");

    } catch (error) {

        console.error(error);

        showMessage(
            "Unable to open skill.",
            "error"
        );
    }
}


/* =========================
   CLOSE MODAL
========================= */

function closeSkillModal() {

    document.getElementById(
        "skillModal"
    ).classList.remove("show");
}


/* =========================
   SAVE SKILL
========================= */

document.getElementById(
    "skillForm"
).addEventListener(
    "submit",
    async function(event) {

        event.preventDefault();

        const skillId =
            document.getElementById(
                "skillId"
            ).value;

        const skillName =
            document.getElementById(
                "skillName"
            ).value.trim();

        const proficiency =
            document.getElementById(
                "proficiency"
            ).value;

        if (!skillName || !proficiency) {

            showMessage(
                "Please enter skill name and proficiency.",
                "error"
            );

            return;
        }

        const skillData = {
            name: skillName,
            proficiency: proficiency
        };

        try {

            let response;

            if (skillId) {

                response = await fetch(
                    `/api/skills/${skillId}`,
                    {
                        method: "PUT",

                        headers: {
                            "Authorization":
                                "Bearer " + token,

                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(skillData)
                    }
                );

            } else {

                response = await fetch(
                    "/api/skills",
                    {
                        method: "POST",

                        headers: {
                            "Authorization":
                                "Bearer " + token,

                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(skillData)
                    }
                );
            }

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
                    "Failed to save skill"
                );
            }

            closeSkillModal();

            showMessage(
                skillId
                    ? "Skill updated successfully."
                    : "Skill added successfully.",
                "success"
            );

            await loadSkills();

        } catch (error) {

            console.error(
                "Save Skill Error:",
                error
            );

            showMessage(
                "Unable to save skill.",
                "error"
            );
        }
    }
);


/* =========================
   DELETE SKILL
========================= */

async function deleteSkill(skillId) {

    if (!confirm(
        "Are you sure you want to delete this skill?"
    )) {
        return;
    }

    try {

        const response = await fetch(
            `/api/skills/${skillId}`,
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
                "Failed to delete skill"
            );
        }

        showMessage(
            "Skill deleted successfully.",
            "success"
        );

        await loadSkills();

    } catch (error) {

        console.error(
            "Delete Skill Error:",
            error
        );

        showMessage(
            "Unable to delete skill.",
            "error"
        );
    }
}


/* =========================
   MESSAGE
========================= */

function showMessage(message, type) {

    const container =
        document.getElementById("message");

    container.innerHTML = `
        <div class="message ${type}">
            ${escapeHTML(message)}
        </div>
    `;

    setTimeout(() => {
        container.innerHTML = "";
    }, 3000);
}


/* =========================
   HTML SAFETY
========================= */

function escapeHTML(value) {

    const div =
        document.createElement("div");

    div.textContent = value ?? "";

    return div.innerHTML;
}


/* =========================
   CLOSE MODAL ON BACKDROP
========================= */

document.getElementById(
    "skillModal"
).addEventListener(
    "click",
    function(event) {

        if (event.target === this) {
            closeSkillModal();
        }

    }
);


/* =========================
   INITIAL LOAD
========================= */

loadSkills();