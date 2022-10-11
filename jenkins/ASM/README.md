# Install Anthos Service Mesh for istio on GKE

This part of the repo uses jenkins pipeline to install Anthos Service Mesh on GKE

### Steps:

1. Create a GKE cluster. You can use the cloud console or as I did it in Terraform. You can find my config in the Terraform folder
#### Note: If using my config, Add secondary ranges to the default subnet in us-central1  region in the deafult network as shown below:
- gke-us-central1-dev-bnet-1 	172.16.64.0/20
- gke-us-central1-cnet-2 	172.16.8.0/23  
2. Create a GCP load balancer. You can use the cloud console or as I did it in Terraform. You can find my config in the Terraform folder
3. Create a Firewall rule as follows
- ```gcloud compute firewall-rules create istio-webhook-test --allow=tcp:443,tcp:8443,tcp:9443,tcp:10250,tcp:10254,tcp:15010,tcp:15012,tcp:15014,tcp:15017 --source-ranges=172.16.0.0/28 --direction=INGRESS --target-tags=gke --priority=1000```
#### Note: The source-range is the Control plane address range of your GKE cluster and target-tag is the same as the network tag on the nodepool. If you use my terraform config, you should be fine
4. Create an external IP and DNS entry for your host
- ```gcloud compute addresses create ADDRESS_NAME --global --ip-version IPV4```
- ```gcloud gcloud dns managed-zones create my-zone --dns-name=my.zone.com. --description="My zone!"```
5. Run the pipeine with the appropriate parameters

### For multi-cluster Anthos Service Mesh

1. Remove comment from scripts.groovy.
