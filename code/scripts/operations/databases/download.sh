#!/bin/sh
# See: https://angelov.ai/post/2020/wget-files-from-gdrive/

echo
echo "---------------------------------------------------------------------------------"
echo "Entering Download Database Backup Files Script"
echo "---------------------------------------------------------------------------------"
echo

echo
echo "Downloading sleek_fsr_testrun_1.backup"
echo


wget --load-cookies /tmp/cookies.txt "https://docs.google.com/uc?export=download&confirm=$(wget --quiet --save-cookies /tmp/cookies.txt --keep-session-cookies --no-check-certificate 'https://docs.google.com/uc?export=download&id=1MdDNFCIzsH6z1Iwdtq2P1Z9EVbXlmmmb' -O- | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1\n/p')&id=1MdDNFCIzsH6z1Iwdtq2P1Z9EVbXlmmmb" -O sleek_fsr_testrun_1.backup && rm -rf /tmp/cookies.txt



echo
echo "Downloading sleek_fsr_testrun_2.backup"
echo "---------------------------------------------------------------------------------"
echo


wget --load-cookies /tmp/cookies.txt "https://docs.google.com/uc?export=download&confirm=$(wget --quiet --save-cookies /tmp/cookies.txt --keep-session-cookies --no-check-certificate 'https://docs.google.com/uc?export=download&id=1E5mLj8oIMYmkxQyGJmoSMHwpq0i7RXLO' -O- | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1\n/p')&id=1E5mLj8oIMYmkxQyGJmoSMHwpq0i7RXLO" -O sleek_fsr_testrun_2.backup && rm -rf /tmp/cookies.txt




echo
echo "---------------------------------------------------------------------------------"
echo "Leaving Download Database Backup Files Script"
echo "---------------------------------------------------------------------------------"
echo

