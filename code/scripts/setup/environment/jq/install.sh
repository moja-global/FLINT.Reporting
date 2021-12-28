echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install jq"
echo "---------------------------------------------------------------------------------"


echo
echo "Update the target system with the required repositories"
yes '' | sudo add-apt-repository ppa:eugenesan/ppa

echo
echo "Updating the local package index"
yes '' | sudo apt update

echo
echo "Installing jq"
sudo apt install jq -y

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install jq"
echo "---------------------------------------------------------------------------------"

