const token =
    localStorage.getItem("edusphere_token");


if (!token) {

    window.location.href =
        "/login.html";
}


/* =========================================
   LOAD DASHBOARD
========================================= */

async function loadDashboard() {

    try {

        const response =
            await fetch(
                "/api/auth/me",
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
                "Failed to load student dashboard"
            );
        }


        const profile =
            await response.json();


        renderDashboard(profile);


    } catch (error) {

        console.error(
            "Dashboard Error:",
            error
        );

        document.getElementById(
            "studentInfo"
        ).textContent =
            "Unable to load student information.";

    }
}


/* =========================================
   RENDER DASHBOARD
========================================= */

function renderDashboard(profile) {

    const student =
        profile.student || {};

    const studentProfile =
        profile.profile || {};

    const projects =
        profile.projects || [];

    const skills =
        profile.skills || [];

    const certifications =
        profile.certifications || [];

    const achievements =
        profile.achievements || [];


    /* ==============================
       STUDENT INFORMATION
    ============================== */

    document.getElementById(
        "studentName"
    ).textContent =
        student.name || "Student";


    document.getElementById(
        "studentInfo"
    ).textContent =
        `${student.registrationNumber || "-"} • ${
            student.department || "-"
        } • Year ${
            student.year ?? "-"
        }`;


    /* ==============================
       ACADEMIC INFORMATION
    ============================== */

    document.getElementById(
        "registrationNumber"
    ).textContent =
        student.registrationNumber || "-";


    document.getElementById(
        "department"
    ).textContent =
        student.department || "-";


    document.getElementById(
        "year"
    ).textContent =
        student.year ?? "-";


    document.getElementById(
        "section"
    ).textContent =
        student.section || "-";


    /* ==============================
       STATISTICS
    ============================== */

    document.getElementById(
        "projectCount"
    ).textContent =
        projects.length;


    document.getElementById(
        "skillCount"
    ).textContent =
        skills.length;


    document.getElementById(
        "certificationCount"
    ).textContent =
        certifications.length;


    document.getElementById(
        "achievementCount"
    ).textContent =
        achievements.length;


    /* ==============================
       PROFILE COMPLETION
    ============================== */

    calculateProfileCompletion(
        studentProfile,
        projects,
        skills,
        certifications,
        achievements
    );
}


/* =========================================
   PROFILE COMPLETION
========================================= */

function calculateProfileCompletion(
    profile,
    projects,
    skills,
    certifications,
    achievements
) {

    let completed = 0;

    const total = 8;


    /*
       1. GitHub
    */

    if (profile.githubUrl) {
        completed++;
    }


    /*
       2. LinkedIn
    */

    if (profile.linkedinUrl) {
        completed++;
    }


    /*
       3. Resume
    */

    if (profile.resumeUrl) {
        completed++;
    }


    /*
       4. LeetCode
    */

    if (profile.leetcodeUrl) {
        completed++;
    }


    /*
       5. CodeChef
    */

    if (profile.codechefUrl) {
        completed++;
    }


    /*
       6. Projects
    */

    if (projects.length > 0) {
        completed++;
    }


    /*
       7. Skills
    */

    if (skills.length > 0) {
        completed++;
    }


    /*
       8. Certifications / Achievements
    */

    if (
        certifications.length > 0 ||
        achievements.length > 0
    ) {

        completed++;

    }


    const percentage =
        Math.round(
            (completed / total) * 100
        );


    document.getElementById(
        "profileCompletion"
    ).textContent =
        percentage;


    document.getElementById(
        "completionProgress"
    ).style.width =
        percentage + "%";
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
   INITIAL LOAD
========================================= */

loadDashboard();