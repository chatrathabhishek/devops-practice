# Jenkins setup

### Deploy Jenkins in cloud

1. Create a VM in GCE. I am using the below config
    - Machine Type: e2-medium
    - Disk : SSD persistent disk - 50GB
    - OS : Ubuntu 20.04.4 LTS
    - Enable Firewall rules for HTTP and HTTPS
    - You can use public IP for ease. I used no public IP for security and IAP for access

2. Install Jenkins on the Ubuntu VM as below:
    - ssh into the vm.
    - To install java```sudo apt-get install openjdk```
    - ```curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo tee \
        /usr/share/keyrings/jenkins-keyring.asc > /dev/null
    - ```echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
        https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
        /etc/apt/sources.list.d/jenkins.list > /dev/null
    - ```sudo apt-get update```
    - ```sudo apt-get install jenkins```

3. Start Jenkins service
    - ```sudo systemctl start jenkins```

### Jenkins is ready to serve

4. Initialize Jenkins
    - Go to <public_ip>:8080 on the web browser if you used public IP on the VM.
    - ssh into server
    - sudo cat /var/lib/docker/volumes/jenkins_home/_data/secrets/initialAdminPassword
    - copy the password and paste it into the web browser to login
    - Install initial plugins
    - Create admin credentials

5. Configure Environments
    - On the dashboard, click on ```Mange Jenkins```
    - Go to ```Global Tool Configuration```
    - ![alt text](https://github.com/chatrathabhishek/devops-practice/tree/main/images/install_tools.png)
        Go to Maven and fill the required info and save.

6. Create a job
    - Click on create new item.
    - Give a name and select Freestyle job.
    - Go to source management
        ![alt text](https://github.com/chatrathabhishek/devops-practice/tree/main/images/source_mgmt.png)
    - You should add your repo info and credentials as shown above. You can specify branch info if you doing a branch else just leave it as main
    - Scroll down to ```Additiona Behaviour``` and click on add and select ```Sparse Checkout paths```
        ![alt text](https://github.com/chatrathabhishek/devops-practice/tree/main/images/addnl_behaviour.png)
    - Scroll down to ```Build``` and click ```Add build step```
        ![alt text](https://github.com/chatrathabhishek/devops-practice/tree/main/images/build.png)
    - Make sure to add the commands as shown above
    - Save

7. Build job
    - On the Dashboard you should be able to see your job. Click on it
    - Go to the left hand menu and select ```Build Now```


###Note: 
If using IAP, be sure to go to ```Manage Jenkins``` -> ```Configure Global Security``` and ```Enable proxy compatibility``` under ```CSRF Protection```
