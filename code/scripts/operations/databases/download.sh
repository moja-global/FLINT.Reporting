#!/bin/sh
# See: https://angelov.ai/post/2020/wget-files-from-gdrive/

echo 
echo "========================================================================"
echo "Entering Test Databases Download Script"
echo "========================================================================"


# ------------------------------------------------------------------------
# INITIALIZE SHELL COLOR VARIABLES
# ------------------------------------------------------------------------

RED_COLOR='\033[0;31m'
GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'

# ------------------------------------------------------------------------
# INITIALIZE PATH VARIABLES
# ------------------------------------------------------------------------

# root/code/scripts/operations/databases
DATABASES_DIR="$(cd "$(dirname "$0")" && pwd)"

# root/code/scripts/operations
OPERATIONS_DIR="$(dirname "$DATABASES_DIR")"

# root/code/scripts
SCRIPTS_DIR="$(dirname "$OPERATIONS_DIR")"

# root/code/
CODE_DIR="$(dirname "$SCRIPTS_DIR")"


# ------------------------------------------------------------------------
# SET DEFAULT WORKING DIRECTORY
# ------------------------------------------------------------------------

cd $CODE_DIR/data



# ------------------------------------------------------------------------
# DOWNLOAD TEST DATABASES
# ------------------------------------------------------------------------

echo
echo "Downloading test_db_1"
echo "------------------------------------------------------------------------"
echo


wget --load-cookies /tmp/cookies.txt "https://docs.google.com/uc?export=download&confirm=$(wget --quiet --save-cookies /tmp/cookies.txt --keep-session-cookies --no-check-certificate 'https://docs.google.com/uc?export=download&id=1MdDNFCIzsH6z1Iwdtq2P1Z9EVbXlmmmb' -O- | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1\n/p')&id=1MdDNFCIzsH6z1Iwdtq2P1Z9EVbXlmmmb" -O test_db_1.backup && rm -rf /tmp/cookies.txt



echo
echo "Downloading test_db_2"
echo "------------------------------------------------------------------------"
echo


wget --load-cookies /tmp/cookies.txt "https://docs.google.com/uc?export=download&confirm=$(wget --quiet --save-cookies /tmp/cookies.txt --keep-session-cookies --no-check-certificate 'https://docs.google.com/uc?export=download&id=1E5mLj8oIMYmkxQyGJmoSMHwpq0i7RXLO' -O- | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1\n/p')&id=1E5mLj8oIMYmkxQyGJmoSMHwpq0i7RXLO" -O test_db_2.backup && rm -rf /tmp/cookies.txt



echo
echo "Downloading test_db_3"
echo "------------------------------------------------------------------------"
echo

wget --load-cookies /tmp/cookies.txt "https://docs.google.com/uc?export=download&confirm=$(wget --quiet --save-cookies /tmp/cookies.txt --keep-session-cookies --no-check-certificate 'https://docs.google.com/uc?export=download&id=1Iaa_SpeCpmGT9VYWuvXqEEKREqwthDTd' -O- | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1\n/p')&id=1Iaa_SpeCpmGT9VYWuvXqEEKREqwthDTd" -O test_db_3.backup && rm -rf /tmp/cookies.txt



echo 
echo "========================================================================"
echo "Leaving Test Databases Download Script"
echo "========================================================================"

