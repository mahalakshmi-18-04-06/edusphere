const loginForm =
    document.getElementById("loginForm");

const errorMessage =
    document.getElementById("errorMessage");

const loginButton =
    document.getElementById("loginButton");

const buttonText =
    document.getElementById("buttonText");

const buttonLoader =
    document.getElementById("buttonLoader");

const togglePassword =
    document.getElementById("togglePassword");

const passwordInput =
    document.getElementById("password");


/* =========================================================
   SHOW / HIDE PASSWORD
   ========================================================= */

togglePassword.addEventListener(
    "click",
    function () {

        if (passwordInput.type === "password") {

            passwordInput.type = "text";

            togglePassword.textContent =
                "Hide";

        } else {

            passwordInput.type = "password";

            togglePassword.textContent =
                "Show";
        }
    }
);


/* =========================================================
   LOGIN
   ========================================================= */

loginForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();

        hideError();

        const rollNumber =
            document
                .getElementById("rollNumber")
                .value
                .trim();

        const password =
            passwordInput.value;


        if (!rollNumber || !password) {

            showError(
                "Please enter your roll number and password."
            );

            return;
        }


        setLoading(true);


        try {

            const response =
                await fetch(
                    "/api/auth/login",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            rollNumber: rollNumber,
                            password: password
                        })
                    }
                );


            const data =
                await response.json();


            if (!response.ok) {

                showError(
                    data.message ||
                    data ||
                    "Invalid roll number or password."
                );

                setLoading(false);

                return;
            }


            /* Save JWT */

            localStorage.setItem(
                "edusphere_token",
                data.token
            );


            /* Save user information */

            localStorage.setItem(
                "edusphere_role",
                data.role
            );

            localStorage.setItem(
                "edusphere_roll_number",
                data.rollNumber
            );


            /* Redirect based on role */

            if (data.role === "ADMIN") {

                window.location.href =
                    "/admin.html";

            } else {

                window.location.href = "/dashboard.html";
            }

        } catch (error) {

            console.error(
                "Login Error:",
                error
            );

            showError(
                "Unable to connect to EduSphere. Please make sure the server is running."
            );

            setLoading(false);
        }
    }
);


/* =========================================================
   ERROR MESSAGE
   ========================================================= */

function showError(message) {

    errorMessage.textContent =
        message;

    errorMessage.style.display =
        "block";
}


function hideError() {

    errorMessage.textContent = "";

    errorMessage.style.display =
        "none";
}


/* =========================================================
   LOADING STATE
   ========================================================= */

function setLoading(isLoading) {

    loginButton.disabled =
        isLoading;

    if (isLoading) {

        buttonText.style.display =
            "none";

        buttonLoader.style.display =
            "inline-block";

    } else {

        buttonText.style.display =
            "inline";

        buttonLoader.style.display =
            "none";
    }
}