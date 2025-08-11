/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form
*/

document.addEventListener("DOMContentLoaded", () => {
  // Load all doctors when the page loads
  loadDoctorCards();

  // Event listeners for search and filter
  document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
  document.getElementById("timeFilter").addEventListener("change", filterDoctorsOnChange);
  document.getElementById("specialtyFilter").addEventListener("change", filterDoctorsOnChange);

  // Add Doctor button (assumes an 'Add Doctor' button with id 'addDoctorBtn' exists)
  const addDoctorBtn = document.getElementById("addDoctorBtn");
  if (addDoctorBtn) {
    addDoctorBtn.addEventListener("click", () => openModal("addDoctor"));
  }
});

// Fetch and render all doctors
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (err) {
    console.error("Error loading doctors:", err);
    document.getElementById("content").innerHTML = "<p>Error loading doctors.</p>";
  }
}

// Filter doctors based on search/filter inputs
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar").value.trim() || null;
  const time = document.getElementById("timeFilter").value || null;
  const specialty = document.getElementById("specialtyFilter").value || null;

  try {
    const doctors = await filterDoctors(name, time, specialty);
    if (doctors && doctors.length > 0) {
      renderDoctorCards(doctors);
    } else {
      document.getElementById("content").innerHTML = "<p>No doctors found with the given filters.</p>";
    }
  } catch (err) {
    alert("Failed to filter doctors. " + err.message);
  }
}

// Helper to generate doctor cards
function renderDoctorCards(doctors) {
  const container = document.getElementById("content");
  container.innerHTML = "";
  doctors.forEach(doc => container.appendChild(createDoctorCard(doc)));
}

// Add doctor from modal form
async function adminAddDoctor() {
  // Collect form data (assumes form fields exist)
  const name = document.getElementById("doctorName").value.trim();
  const email = document.getElementById("doctorEmail").value.trim();
  const phone = document.getElementById("doctorPhone").value.trim();
  const password = document.getElementById("doctorPassword").value.trim();
  const specialty = document.getElementById("doctorSpecialty").value.trim();
  const availableTimes = document.getElementById("doctorAvailableTimes").value.split(",").map(t => t.trim());

  const token = localStorage.getItem("token");
  if (!token) {
    alert("Admin authentication required.");
    return;
  }

  const doctor = {
    name,
    email,
    phone,
    password,
    specialty,
    availableTimes
  };

  try {
    const response = await saveDoctor(doctor, token);
    alert("Doctor added successfully!");
    closeModal();
    // Optionally reload doctor cards
    loadDoctorCards();
  } catch (err) {
    alert("Error adding doctor: " + (err.message || err));
  }
}

// Placeholder service functions (implement these as needed)
async function getDoctors() {
  // Example: Fetch doctors from API
  const response = await fetch("/api/doctor/");
  if (!response.ok) throw new Error("Failed to fetch doctors");
  return await response.json();
}

async function filterDoctors(name, time, specialty) {
  // Example: call filter endpoint with query params
  let url = `/api/doctor/filter/${name || "null"}/${time || "null"}/${specialty || "null"}`;
  const response = await fetch(url);
  if (!response.ok) throw new Error("Failed to filter doctors");
  return await response.json();
}

async function saveDoctor(doctor, token) {
  const response = await fetch(`/api/doctor/register/${token}`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(doctor)
  });
  if (!response.ok) throw new Error("Failed to add doctor");
  return await response.json();
}

// Example doctor card
function createDoctorCard(doctor) {
  const card = document.createElement("div");
  card.className = "doctor-card";
  card.innerHTML = `
    <h3>${doctor.name}</h3>
    <p>Email: ${doctor.email}</p>
    <p>Phone: ${doctor.phone}</p>
    <p>Specialty: ${doctor.specialty}</p>
    <p>Available Times: ${Array.isArray(doctor.availableTimes) ? doctor.availableTimes.join(", ") : doctor.availableTimes}</p>
  `;
  return card;
}

// Modal helpers (implement openModal and closeModal as needed)
function openModal(type) {
  document.getElementById("modal-overlay").style.display = "block";
  // Populate modal-inner-content based on type
}
function closeModal() {
  document.getElementById("modal-overlay").style.display = "none";
}