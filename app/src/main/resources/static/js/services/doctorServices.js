// doctorServices.js

// Base API endpoint for doctors
const DOCTOR_API = "/api/doctor";

// Fetch all doctors
export async function getDoctors() {
  try {
    const response = await fetch(`${DOCTOR_API}/`);
    if (!response.ok) throw new Error("Failed to fetch doctors");
    const data = await response.json();
    // Expecting response: { doctors: [...] }
    return data.doctors || [];
  } catch (err) {
    console.error("getDoctors error:", err);
    return [];
  }
}

// Delete a doctor by ID (requires admin token)
export async function deleteDoctor(doctorId, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/delete/${doctorId}/${token}`, {
      method: "DELETE"
    });
    const data = await response.json();
    if (response.ok) {
      return { success: true, message: data.message || "Doctor deleted successfully." };
    } else {
      return { success: false, message: data.message || "Failed to delete doctor." };
    }
  } catch (err) {
    console.error("deleteDoctor error:", err);
    return { success: false, message: "Error deleting doctor." };
  }
}

// Create a new doctor (requires admin token)
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/register/${token}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor)
    });
    const data = await response.json();
    if (response.ok) {
      return { success: true, message: data.message || "Doctor created successfully." };
    } else {
      return { success: false, message: data.message || "Failed to create doctor." };
    }
  } catch (err) {
    console.error("saveDoctor error:", err);
    return { success: false, message: "Error saving doctor." };
  }
}

// Filter doctors by name, time, and specialty
export async function filterDoctors(name, time, specialty) {
  try {
    // Use "null" for parameters not set, as the backend expects
    const n = name || "null";
    const t = time || "null";
    const s = specialty || "null";
    const response = await fetch(`${DOCTOR_API}/filter/${n}/${t}/${s}`);
    if (response.ok) {
      const data = await response.json();
      // Backend returns: { doctors: [...] }
      return data.doctors || [];
    } else {
      console.error("filterDoctors failed:", response.statusText);
      return [];
    }
  } catch (err) {
    alert("Error filtering doctors. Please try again.");
    return [];
  }
}