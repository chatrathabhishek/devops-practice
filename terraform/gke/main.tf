terraform {
  backend "gcs" {
    bucket = "" // storage bucket
    prefix = "gke"
  }
}

data "google_project" "service_project" {
  provider   = google-beta
  project_id = var.project
}

data "google_kms_key_ring" "key_ring" {
  provider = google-beta
  count    = var.key != "" ? 1 : 0
  project  = var.project
  name     = var.key_ring
  location = var.key_ring_region
}

data "google_kms_crypto_key" "crypto_key" {
  provider = google-beta
  count    = var.key != "" ? 1 : 0
  name     = var.key
  key_ring = data.google_kms_key_ring.key_ring[count.index].id
}

#permissions to encrypt and decrypt Etcd backend
resource "google_project_iam_member" "container_engine_robot_binding" {
  provider = google-beta
  project  = data.google_project.service_project.project_id
  role     = "roles/cloudkms.cryptoKeyEncrypterDecrypter"
  member   = "serviceAccount:service-${data.google_project.service_project.number}@container-engine-robot.iam.gserviceaccount.com"
}

module "gke-cluster" {
  source                                = "" // gke resource module with the following configs
  project                               = var.project
  cluster_name                          = var.cluster_name
  location                              = var.location
  network                               = var.network
  subnetwork                            = var.subnetwork
  networking_mode                       = var.networking_mode
  set_ip_allocation_policy              = true
  cluster_secondary_range_name          = var.cluster_secondary_range_name
  services_secondary_range_name         = var.services_secondary_range_name
  set_master_authorized_networks_config = true
  cidr_blocks                           = var.cidr_blocks
  enable_config_connector_config        = true
  vertical_pod_autoscaling_enabled      = true
  set_authenticator_groups_config       = true
  workload_identity_config_enabled      = true
  enable_database_encryption            = true
  default_max_pods_per_node             = 16
  state                                 = var.state
  key_name                              = data.google_kms_crypto_key.crypto_key[0].id
  master_ipv4_cidr_block                = var.master_ipv4_cidr_block
  service_account                       = var.service_account
  oauth_scopes                          = var.oauth_scopes
  image_type                            = var.image_type
  default_node_label                    = var.default_node_label
  disable_network_policy_config         = false
  network_policy_provider               = "CALICO"
  disable_http_load_balancing           = false

  metadata = {
    "disable-legacy-endpoints" = "true"
  }

}

module "gke-1-node-pools" {
  source = "" // gke resource module with the following configs

  project         = var.project
  region          = var.location
  cluster_name    = ["ac-gke"]
  set_node_config = true
  node_pool = [
    {
      "name"                      = "gke-node-pool-1"
      "machine_type"              = "n2-standard-8"
      "max_node_count"            = "2"
      "min_node_count"            = "1"
      "initial_node_count"        = "1"
      "workload_identity_enabled" = "true"
      "auto_upgrade"              = "true"
    },
  ]
  oauth_scopes                 = var.oauth_scopes
  set_shielded_instance_config = true
  enable_integrity_monitoring  = true
  enable_secure_boot           = true
  network_tags                 = ["gke"]
  node_labels = {
    product = "gke"
    env     = "dev"
    mesh_id = "proj-${data.google_project.service_project.number}"
  }
  depends_on = [
    module.gke-cluster,
  ]
}
