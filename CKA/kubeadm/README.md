# Kubeadm

kubeadm is a way to setup and bootstrap your own kubernetes cluster.

For our installation we are using linux EC2 instances on AWS
1. Medium EC2 instance for the Control plane node
2. Large EC2 isntance for the worker nodes. Atleast 2

kubeadm preqs and installation steps can be found on [here](https://v1-32.docs.kubernetes.io/docs/setup/production-environment/tools/kubeadm/install-kubeadm/)

Once done with the preqs, you can find scripts to install containerd and kubernetes components(kubeadm, kubelet, kubectl) in here. Please follow documentaion and use the scripts to setup your own kubernetes cluster.

Run below comand to set kube config so that it can work with kubectl

`sudo cp -i /etc/kubernetes/admin.conf ~/.kube/config`
