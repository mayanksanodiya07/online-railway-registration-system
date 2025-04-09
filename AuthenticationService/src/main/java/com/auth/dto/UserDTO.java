package com.auth.dto;

import java.time.LocalDateTime;
  
public class UserDTO {
	  
	    private Long id;
	    private String username;
	    private String fullName;
	    private String email;
	    private String phoneNumber;
	    private Role role;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
  
		public Role getRole() {
			return role;
		}

		public void setRole(String role) {
	        this.role = Role.valueOf(role.toUpperCase());
	    }

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}

		@Override
		public String toString() {
			return "User [id=" + id + ", username=" + username + ", fullName=" + fullName
					+ ", email=" + email + ", phoneNumber=" + phoneNumber + ", role=" + role + ", createdAt=" + createdAt
					+ ", updatedAt=" + updatedAt + "]";
		}
}