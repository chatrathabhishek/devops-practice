output "public_address" {
  description = "public ip addresses"
  value       = data.google_compute_global_address.public_address.*.address
  sensitive   = true
}

output "google_compute_global_forwarding_rule" {
  description = "forwarding rule"
  value       = google_compute_global_forwarding_rule.https_redirect.*.id
}

output "google_compute_target_http_proxy" {
  description = "forwarding rule"
  value       = google_compute_target_http_proxy.https_redirect.*.id
}

output "google_compute_url_map" {
  description = "forwarding rule"
  value       = google_compute_url_map.https_redirect.*.id
}

output "google_compute_ssl_policy_gke-ssl-policy" {
  description = "SSL policy"
  value       = google_compute_ssl_policy.gke-ssl-policy.enabled_features
}
