terraform {
  required_version = "~> 1.0.2"
  required_providers {
    google-beta = {
      source  = "hashicorp/google-beta"
      version = "~> 4.30.0"
    }
  }
}
