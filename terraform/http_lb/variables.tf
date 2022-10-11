variable "project_id" {
  type        = string
  description = "GCP project id name"
  default     = "gsre-mgmt-global-dev"
}

variable "gcp_region" {
  type        = string
  description = "gcp region"
  default     = "us-central1"
}

variable "env" {
  type    = string
  default = "dev"
}

variable "network_project" {
  type        = string
  description = "GCP project id"
  default     = "its-saas-mgmt-cug01-prep"
}

variable "network" {
  type        = string
  description = "shared vpc"
  default     = "its-saas-mgmt-cug01-prep-vpc"
}

variable "external_ip" {
  type    = list(any)
  default = []
}
