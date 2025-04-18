package com.auth.dto;

import java.time.LocalDateTime;
import java.util.List;
  
public class UserDTO {
	  
	    private Long id;
	    private Long authUserId;
	    private String username;
	    private String fullName;
	    private String email;
	    private String phoneNumber;
	    private List<String> roles;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getAuthUserId() {
			return authUserId;
		}

		public void setAuthUserId(Long authUserId) {
			this.authUserId = authUserId;
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

		public List<String> getRoles() {
			return roles;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
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
					+ ", email=" + email + ", phoneNumber=" + phoneNumber + ", roles=" + roles + ", createdAt=" + createdAt
					+ ", updatedAt=" + updatedAt + "]";
		}
}