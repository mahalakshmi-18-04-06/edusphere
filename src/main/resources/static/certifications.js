const token = localStorage.getItem("edusphere_token");

if (!token) {
    window.location.href = "/login.html";
}

const certificationsContainer =
    document.getElementById("certificationsContainer");

const certificationModal =
    document.getElementById("certificationModal");

const certificationForm =
    document.getElementById("certificationForm");

const message =
    document.getElementById("message");

let certifications = [];


async function loadCertifications() {

    try {

        const response = await fetch(
            "/api/certifications/me",
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
                "Failed to load certifications"
            );
        }

        certifications = await response.json();

        renderCertifications();

    } catch (error) {

        showMessage(
            "Unable to load certifications.",
            "error"
        );
    }
}


function renderCertifications() {

    certificationsContainer.innerHTML = "";

    if (certifications.length === 0) {

        certificationsContainer.innerHTML = `
            <p>No certifications added yet.</p>
        `;

        return;
    }

    certifications.forEach(certification => {

        const card =
            document.createElement("div");

        card.className =
            "certification-card";

        const issueDate =
            certification.issueDate
                ? formatDate(
                    certification.issueDate
                )
                : "Date not provided";

        let links = "";

        if (certification.credentialUrl) {

            links += `
                <a
                    href="${escapeAttribute(
                        certification.credentialUrl
                    )}"
                    target="_blank"
                    rel="noopener noreferrer">
                    View Credential →
                </a>
            `;
        }

        if (certification.certificateImageUrl) {

            links += `
                <a
                    href="${escapeAttribute(
                        certification.certificateImageUrl
                    )}"
                    target="_blank"
                    rel="noopener noreferrer">
                    View Certificate →
                </a>
            `;
        }

        let image = "";

        if (certification.certificateImageUrl) {

            image = `
                <img
                    class="certificate-image"
                    src="${escapeAttribute(
                        certification.certificateImageUrl
                    )}"
                    alt="Certificate"
                    onerror="this.style.display='none'">
            `;
        }

        card.innerHTML = `

            <div class="certification-icon">
                ✓
            </div>

            <h3>
                ${escapeHTML(
                    certification.title
                )}
            </h3>

            <p class="organization">
                ${escapeHTML(
                    certification.issuingOrganization
                )}
            </p>

            <p class="issue-date">
                Issued:
                ${issueDate}
            </p>

            ${image}

            <div class="certification-links">
                ${links}
            </div>

            <div class="certification-actions">

                <button
                    class="edit-btn"
                    onclick="openEditCertification(
                        ${certification.id}
                    )">
                    Edit
                </button>

                <button
                    class="delete-btn"
                    onclick="deleteCertification(
                        ${certification.id}
                    )">
                    Delete
                </button>

            </div>
        `;

        certificationsContainer.appendChild(card);
    });
}


function openAddCertification() {

    document.getElementById("modalTitle")
        .textContent = "Add Certification";

    document.getElementById("certificationId")
        .value = "";

    document.getElementById("title")
        .value = "";

    document.getElementById("issuingOrganization")
        .value = "";

    document.getElementById("issueDate")
        .value = "";

    document.getElementById("credentialUrl")
        .value = "";

    document.getElementById("certificateImageUrl")
        .value = "";

    certificationModal.style.display = "flex";
}


function openEditCertification(id) {

    const certification =
        certifications.find(
            item => item.id === id
        );

    if (!certification) {
        return;
    }

    document.getElementById("modalTitle")
        .textContent = "Edit Certification";

    document.getElementById("certificationId")
        .value = certification.id;

    document.getElementById("title")
        .value = certification.title || "";

    document.getElementById("issuingOrganization")
        .value =
            certification.issuingOrganization || "";

    document.getElementById("issueDate")
        .value =
            certification.issueDate || "";

    document.getElementById("credentialUrl")
        .value =
            certification.credentialUrl || "";

    document.getElementById("certificateImageUrl")
        .value =
            certification.certificateImageUrl || "";

    certificationModal.style.display = "flex";
}


function closeCertificationModal() {

    certificationModal.style.display = "none";
}


certificationForm.addEventListener(
    "submit",
    async function(event) {

        event.preventDefault();

        const id =
            document.getElementById(
                "certificationId"
            ).value;

        const certificationData = {

            title:
                document.getElementById(
                    "title"
                ).value.trim(),

            issuingOrganization:
                document.getElementById(
                    "issuingOrganization"
                ).value.trim(),

            issueDate:
                document.getElementById(
                    "issueDate"
                ).value,

            credentialUrl:
                document.getElementById(
                    "credentialUrl"
                ).value.trim() || null,

            certificateImageUrl:
                document.getElementById(
                    "certificateImageUrl"
                ).value.trim() || null
        };

        try {

            const url = id
                ? `/api/certifications/${id}`
                : "/api/certifications";

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
                            certificationData
                        )
                }
            );

            if (!response.ok) {
                throw new Error(
                    "Failed to save certification"
                );
            }

            closeCertificationModal();

            showMessage(
                id
                    ? "Certification updated successfully."
                    : "Certification added successfully.",
                "success"
            );

            await loadCertifications();

        } catch (error) {

            showMessage(
                "Unable to save certification.",
                "error"
            );
        }
    }
);


async function deleteCertification(id) {

    if (!confirm(
        "Are you sure you want to delete this certification?"
    )) {
        return;
    }

    try {

        const response = await fetch(
            `/api/certifications/${id}`,
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
            "Certification deleted successfully.",
            "success"
        );

        await loadCertifications();

    } catch (error) {

        showMessage(
            "Unable to delete certification.",
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


certificationModal.addEventListener(
    "click",
    function(event) {

        if (event.target === certificationModal) {
            closeCertificationModal();
        }
    }
);


loadCertifications();