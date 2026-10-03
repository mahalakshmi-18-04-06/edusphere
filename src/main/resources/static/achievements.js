const token = localStorage.getItem("edusphere_token");

if (!token) {
    window.location.href = "/login.html";
}

const achievementsContainer =
    document.getElementById("achievementsContainer");

const achievementModal =
    document.getElementById("achievementModal");

const achievementForm =
    document.getElementById("achievementForm");

const message =
    document.getElementById("message");

let achievements = [];


async function loadAchievements() {

    try {

        const response = await fetch(
            "/api/achievements/me",
            {
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (
            response.status === 401 ||
            response.status === 403
        ) {
            localStorage.removeItem("edusphere_token");
            window.location.href = "/login.html";
            return;
        }

        if (!response.ok) {
            throw new Error(
                "Failed to load achievements"
            );
        }

        achievements = await response.json();

        renderAchievements();

    } catch (error) {

        showMessage(
            "Unable to load achievements.",
            "error"
        );
    }
}


function renderAchievements() {

    achievementsContainer.innerHTML = "";

    if (achievements.length === 0) {

        achievementsContainer.innerHTML = `
            <p>No achievements added yet.</p>
        `;

        return;
    }

    achievements.forEach(achievement => {

        const card =
            document.createElement("div");

        card.className =
            "achievement-card";

        const achievementDate =
            achievement.achievementDate
                ? formatDate(
                    achievement.achievementDate
                )
                : "Date not provided";

        let proofLink = "";

        if (achievement.proofUrl) {

            proofLink = `
                <a
                    class="proof-link"
                    href="${escapeAttribute(
                        achievement.proofUrl
                    )}"
                    target="_blank"
                    rel="noopener noreferrer">
                    View Proof →
                </a>
            `;
        }

        card.innerHTML = `

            <div class="achievement-top">

                <div class="achievement-icon">
                    ★
                </div>

                <span class="achievement-type">
                    ${escapeHTML(
                        achievement.achievementType
                    )}
                </span>

            </div>

            <h3>
                ${escapeHTML(
                    achievement.title
                )}
            </h3>

            <p class="description">
                ${escapeHTML(
                    achievement.description
                )}
            </p>

            <p class="achievement-date">
                Achieved:
                ${achievementDate}
            </p>

            ${proofLink}

            <div class="achievement-actions">

                <button
                    class="edit-btn"
                    onclick="openEditAchievement(
                        ${achievement.id}
                    )">
                    Edit
                </button>

                <button
                    class="delete-btn"
                    onclick="deleteAchievement(
                        ${achievement.id}
                    )">
                    Delete
                </button>

            </div>

        `;

        achievementsContainer.appendChild(card);
    });
}


function openAddAchievement() {

    document.getElementById("modalTitle")
        .textContent = "Add Achievement";

    document.getElementById("achievementId")
        .value = "";

    document.getElementById("title")
        .value = "";

    document.getElementById("description")
        .value = "";

    document.getElementById("achievementDate")
        .value = "";

    document.getElementById("achievementType")
        .value = "";

    document.getElementById("proofUrl")
        .value = "";

    achievementModal.style.display = "flex";
}


function openEditAchievement(id) {

    const achievement =
        achievements.find(
            item => item.id === id
        );

    if (!achievement) {
        return;
    }

    document.getElementById("modalTitle")
        .textContent = "Edit Achievement";

    document.getElementById("achievementId")
        .value = achievement.id;

    document.getElementById("title")
        .value = achievement.title || "";

    document.getElementById("description")
        .value = achievement.description || "";

    document.getElementById("achievementDate")
        .value =
            achievement.achievementDate || "";

    document.getElementById("achievementType")
        .value =
            achievement.achievementType || "";

    document.getElementById("proofUrl")
        .value =
            achievement.proofUrl || "";

    achievementModal.style.display = "flex";
}


function closeAchievementModal() {

    achievementModal.style.display = "none";
}


achievementForm.addEventListener(
    "submit",
    async function(event) {

        event.preventDefault();

        const id =
            document.getElementById(
                "achievementId"
            ).value;

        const achievementData = {

            title:
                document.getElementById(
                    "title"
                ).value.trim(),

            description:
                document.getElementById(
                    "description"
                ).value.trim(),

            achievementDate:
                document.getElementById(
                    "achievementDate"
                ).value,

            achievementType:
                document.getElementById(
                    "achievementType"
                ).value,

            proofUrl:
                document.getElementById(
                    "proofUrl"
                ).value.trim() || null
        };

        try {

            const url = id
                ? `/api/achievements/${id}`
                : "/api/achievements";

            const method = id
                ? "PUT"
                : "POST";

            const response = await fetch(
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
                            achievementData
                        )
                }
            );

            if (!response.ok) {
                throw new Error(
                    "Failed to save achievement"
                );
            }

            closeAchievementModal();

            showMessage(
                id
                    ? "Achievement updated successfully."
                    : "Achievement added successfully.",
                "success"
            );

            await loadAchievements();

        } catch (error) {

            showMessage(
                "Unable to save achievement.",
                "error"
            );
        }
    }
);


async function deleteAchievement(id) {

    if (!confirm(
        "Are you sure you want to delete this achievement?"
    )) {
        return;
    }

    try {

        const response = await fetch(
            `/api/achievements/${id}`,
            {
                method: "DELETE",

                headers: {
                    "Authorization":
                        "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error(
                "Delete failed"
            );
        }

        showMessage(
            "Achievement deleted successfully.",
            "success"
        );

        await loadAchievements();

    } catch (error) {

        showMessage(
            "Unable to delete achievement.",
            "error"
        );
    }
}


function formatDate(dateString) {

    const date =
        new Date(
            dateString + "T00:00:00"
        );

    return date.toLocaleDateString(
        "en-IN",
        {
            day: "2-digit",
            month: "short",
            year: "numeric"
        }
    );
}


function showMessage(text, type) {

    message.innerHTML =
        `<div class="${type}">${text}</div>`;

    setTimeout(() => {
        message.innerHTML = "";
    }, 3000);
}


function escapeHTML(value) {

    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


function escapeAttribute(value) {

    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/"/g, "&quot;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}


achievementModal.addEventListener(
    "click",
    function(event) {

        if (event.target === achievementModal) {
            closeAchievementModal();
        }
    }
);


loadAchievements();