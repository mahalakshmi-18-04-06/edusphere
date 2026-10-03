const signupForm =
    document.getElementById("signupForm");

const message =
    document.getElementById("message");

const signupButton =
    document.getElementById("signupButton");


signupForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();

        message.style.display = "none";

        const rollNumber =
            document.getElementById("rollNumber")
                .value
                .trim();

        const name =
            document.getElementById("name")
                .value
                .trim();

        const email =
            document.getElementById("email")
                .value
                .trim();

        const department =
            document.getElementById("department")
                .value;

        const year =
            Number(
                document.getElementById("year")
                    .value
            );

        const section =
            document.getElementById("section")
                .value;

        const password =
            document.getElementById("password")
                .value;

        const confirmPassword =
            document.getElementById("confirmPassword")
                .value;


        /* Password check */

        if (password !== confirmPassword) {

            showMessage(
                "Passwords do not match.",
                "error"
            );

            return;
        }


        signupButton.disabled = true;

        signupButton.textContent =
            "Creating Account...";


        try {

            const response =
                await fetch(
                    "/api/auth/signup",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({

                            rollNumber:
                                rollNumber,

                            password:
                                password,

                            confirmPassword:
                                confirmPassword,

                            name:
                                name,

                            email:
                                email,

                            department:
                                department,

                            year:
                                year,

                            section:
                                section
                        })
                    }
                );


            const data =
                await response.json();


            if (!response.ok) {

                showMessage(
                    data.message ||
                    data ||
                    "Registration failed.",
                    "error"
                );

                signupButton.disabled =
                    false;

                signupButton.textContent =
                    "Create Account";

                return;
            }


            /* Save JWT */

            localStorage.setItem(
                "edusphere_token",
                data.token
            );


            /* Save role */

            localStorage.setItem(
                "edusphere_role",
                data.role
            );


            localStorage.setItem(
                "edusphere_roll_number",
                data.rollNumber
            );


            showMessage(
                "Account created successfully. Redirecting...",
                "success"
            );


            setTimeout(
                function () {

                    window.location.href =
                        "/index.html";

                },
                1000
            );


        } catch (error) {

            console.error(
                "Signup Error:",
                error
            );

            showMessage(
                "Unable to connect to the server.",
                "error"
            );

            signupButton.disabled =
                false;

            signupButton.textContent =
                "Create Account";
        }
    }
);


function showMessage(
    text,
    type
) {

    message.textContent =
        text;

    message.className =
        "message " + type;

    message.style.display =
        "block";
}