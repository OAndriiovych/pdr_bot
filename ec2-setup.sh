yum update
amazon-linux-extras install java-openjdk11
yum install postgresql
yum install ruby
yum install wget
cd /home/ec2-user
wget https://aws-codedeploy-eu-central-1.s3.eu-central-1.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
yum install amazon-cloudwatch-agent
/opt/aws/amazon-cloudwatch-agent/bin amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c ssm:AmazonCloudWatch-linux-dev -s