variable "project" {
  type        = string
  description = "Name of the project"
}

variable "cluster_name" {
  type        = string
  description = "Name of the cluster"
}

variable "location" {
  type        = string
  description = "Location of the cluster"
}

variable "network" {
  type        = string
  description = "The name or self_link of the Google Compute Engine network to which the cluster is connected. For Shared VPC, set this to the self link of the shared network."
}

variable "subnetwork" {
  type        = string
  description = "The name or self_link of the Google Compute Engine subnetwork in which the cluster's instances are launched."
}

variable "networking_mode" {
  type        = string
  description = "Determines whether alias IPs or routes will be used for pod IPs in the cluster. Options are VPC_NATIVE or ROUTES."
}

variable "cluster_secondary_range_name" {
  type        = string
  description = "The name of the existing secondary range in the cluster's subnetwork to use for pod IP addresses."
}

variable "services_secondary_range_name" {
  type        = string
  description = "The name of the existing secondary range in the cluster's subnetwork to use for service ClusterIPs."
}

variable "cidr_blocks" {
  type = list(object({
    cidr_block   = string
    display_name = string
  }))
  description = "External networks that can access the Kubernetes cluster master through HTTPS."
}

variable "state" {
  type        = string
  description = "ENCRYPTED or DECRYPTED."
}

variable "key" {
  type        = string
  description = "The key to use to encrypt/decrypt secrets."
}

variable "key_ring" {
  type        = string
  description = "The key_ring to use to encrypt/decrypt secrets."
}

variable "key_ring_region" {
  type        = string
  description = "The location where key_ring is located."
}

variable "master_ipv4_cidr_block" {
  type        = string
  description = "he IP range in CIDR notation to use for the hosted master network. This range will be used for assigning private IP addresses to the cluster master(s) and the ILB VIP. This range must not overlap with any other ranges in use within the cluster's network, and it must be a /28 subnet."
}

variable "service_account" {
  type        = string
  description = "The service account to be used by the Node VMs. If not specified, the default service account is used."
}

variable "oauth_scopes" {
  type        = list(string)
  description = "The set of Google API scopes to be made available on all of the node VMs under the default service account."
}

variable "image_type" {
  type        = string
  description = "The image type to use for this node."
}

variable "default_node_label" {
  type        = map(any)
  description = "The Kubernetes labels (key/value pairs) to be applied to each node."
}
