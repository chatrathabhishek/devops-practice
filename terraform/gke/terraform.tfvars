project                       = "" // GCP Project
cluster_name                  = "ac-gke"
location                      = "us-central1"
network                       = "default"
subnetwork                    = "default"
networking_mode               = "VPC_NATIVE"
cluster_secondary_range_name  = "gke-us-central1-dev-bnet-1" //IP range : 172.16.64.0/20
services_secondary_range_name = "gke-us-central1-cnet-2"     //IP range : 172.16.8.0/23

cidr_blocks = [{
  cidr_block   = "10.0.0.0/8"
  display_name = "10-0-0-0-8"
  },
  {
    cidr_block   = "172.16.0.0/12"
    display_name = "172-16-0-0-12"
  },
]
state                  = "ENCRYPTED"
key                    = "gke-1"
key_ring               = "gke"
key_ring_region        = "us-central1"
master_ipv4_cidr_block = "172.16.0.0/28"
service_account        = "default"
oauth_scopes = [
  "https://www.googleapis.com/auth/cloud-platform",
  "https://www.googleapis.com/auth/devstorage.read_only",
  "https://www.googleapis.com/auth/logging.write",
  "https://www.googleapis.com/auth/monitoring",
]
image_type = "COS_CONTAINERD"
default_node_label = {
  product  = "gke"
  reviewed = "gsre"
  "asmv"   = "1-13-4-asm-4"
}
