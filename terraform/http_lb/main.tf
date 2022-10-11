terraform {
  backend "gcs" {
    bucket = "" //Storage bucket
    prefix = "http_lb"
  }
}

provider "google" {
  region  = var.gcp_region
  project = var.project_id
}


data "google_compute_global_address" "public_address" {
  count   = length(var.external_ip)
  name    = var.external_ip[count.index]
  project = var.project_id
}

resource "google_compute_global_forwarding_rule" "https_redirect" {
  count                 = length(var.external_ip)
  project               = var.project_id
  name                  = "http-to-https-redirect"
  ip_address            = data.google_compute_global_address.public_address[count.index].address
  port_range            = "80"
  target                = google_compute_target_http_proxy.https_redirect.id
  load_balancing_scheme = "EXTERNAL"
}

resource "google_compute_target_http_proxy" "https_redirect" {
  project     = var.project_id
  provider    = google-beta
  name        = "http-to-https-redirect"
  description = "http to https redirect"
  url_map     = google_compute_url_map.https_redirect.self_link
}

resource "google_compute_url_map" "https_redirect" {
  project = var.project_id
  name    = "http-to-https-redirect"
  default_url_redirect {
    https_redirect         = true
    redirect_response_code = "MOVED_PERMANENTLY_DEFAULT"
    strip_query            = false
  }
}

resource "google_compute_ssl_policy" "gke-ssl-policy" {
  name            = "gke-ssl-policy"
  profile         = "CUSTOM"
  min_tls_version = "TLS_1_2"
  #only enabling strong ciphers
  custom_features = [
    "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
    "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
    "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256",
    "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
    "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
    "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256"
  ]
}
