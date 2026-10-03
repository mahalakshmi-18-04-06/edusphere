const token =
    localStorage.getItem("edusphere_token");

if (!token) {
    window.location.href = "/login.html";
}


// LOAD CURRENT PROFILE

async function loadProfile() {

    try {

        const response = await fetch(
            "/api/auth/me",
            {
                headers: {
                    "Authorization":
                        "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error(
                "Unable to load profile"
            );
        }

        const data =
            await response.json();

        const profile =
            data.profile;

        if (!profile) {
            return;
        }

        document.getElementById(
            "profileImageUrl"
        ).value =
            profile.profileImageUrl || "";

        document.getElementById(
            "githubUrl"
        ).value =
            profile.githubUrl || "";

        document.getElementById(
            "linkedinUrl"
        ).value =
            profile.linkedinUrl || "";

        document.getElementById(
            "portfolioUrl"
        ).value =
            profile.portfolioUrl || "";

        document.getElementById(
            "resumeUrl"
        ).value =
            profile.resumeUrl || "";

        document.getElementById(
            "leetcodeUrl"
        ).value =
            profile.leetcodeUrl || "";

        document.getElementById(
            "codechefUrl"
        ).value =
            profile.codechefUrl || "";

    } catch (error) {

        showMessage(
            error.message,
            true
        );

    }
}


// SAVE PROFILE

document
    .getElementById("profileForm")
    .addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();


            const request = {

                profileImageUrl:
                    document.getElementById(
                        "profileImageUrl"
                    ).value.trim(),

                githubUrl:
                    document.getElementById(
                        "githubUrl"
                    ).value.trim(),

                linkedinUrl:
                    document.getElementById(
                        "linkedinUrl"
                    ).value.trim(),

                portfolioUrl:
                    document.getElementById(
                        "portfolioUrl"
                    ).value.trim(),

                resumeUrl:
                    document.getElementById(
                        "resumeUrl"
                    ).value.trim(),

                leetcodeUrl:
                    document.getElementById(
                        "leetcodeUrl"
                    ).value.trim(),

                codechefUrl:
                    document.getElementById(
                        "codechefUrl"
                    ).value.trim()
            };


            try {

                const response =
                    await fetch(
                        "/api/profiles/me",
                        {
                            method: "PUT",

                            headers: {
                                "Content-Type":
                                    "application/json",

                                "Authorization":
                                    "Bearer " + token
                            },

                            body:
                                JSON.stringify(request)
                        }
                    );


                if (!response.ok) {

                    const error =
                        await response.text();

                    throw new Error(
                        error ||
                        "Unable to update profile"
                    );
                }


                showMessage(
                    "Profile updated successfully!",
                    false
                );


            } catch (error) {

                showMessage(
                    error.message,
                    true
                );

            }

        }
    );


// MESSAGE

function showMessage(
    message,
    isError
) {

    const element =
        document.getElementById(
            "message"
        );

    element.textContent = message;

    element.style.color =
        isError
            ? "#dc2626"
            : "#16a34a";
}


// LOAD

loadProfile();