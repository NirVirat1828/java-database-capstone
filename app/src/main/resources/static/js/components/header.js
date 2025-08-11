// header.js

export function renderHeader() {
  const headerDiv = document.getElementById("header");
  if (!headerDiv) return;

  // If on root, reset session & show basic header
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CMS Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>
    `;
    return;
  }

  // Get session info
  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  // Basic header
  let headerContent = `<header class="header">
    <div class="logo-section">
      <img src="../assets/images/logo/logo.png" alt="Hospital CMS Logo" class="logo-img">
      <span class="logo-title">Hospital CMS</span>
    </div>
    <nav>
  `;

  // Session expired or invalid
  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/";
    return;
  }

  // Role-specific nav actions
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
      <a href="#" onclick="logout()">Logout</a>
    `;
  } else if (role === "doctor") {
    headerContent += `
      <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
      <a href="#" onclick="logout()">Logout</a>
    `;
  } else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>
    `;
  } else if (role === "loggedPatient") {
    headerContent += `
      <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
      <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
      <a href="#" onclick="logoutPatient()">Logout</a>
    `;
  }

  headerContent += `</nav></header>`;
  headerDiv.innerHTML = headerContent;

  attachHeaderButtonListeners();
}

// Attach listeners to header buttons (like login/signup)
function attachHeaderButtonListeners() {
  const loginBtn = document.getElementById("patientLogin");
  if (loginBtn) {
    loginBtn.addEventListener("click", () => openModal("patientLogin"));
  }
  const signupBtn = document.getElementById("patientSignup");
  if (signupBtn) {
    signupBtn.addEventListener("click", () => openModal("patientSignup"));
  }
}

// Logout functions for admin/doctor and patient
window.logout = function () {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/";
};
window.logoutPatient = function () {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/pages/index.html";
};

// Render the header on DOMContentLoaded
if (document.readyState === "complete" || document.readyState === "interactive") {
  renderHeader();
} else {
  window.addEventListener("DOMContentLoaded", renderHeader);
}