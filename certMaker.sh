#!/bin/bash

SERVER=$1
PASSWORD=$2
CLASSPATH=bcprov-jdk15on-146.jar
CERTSTORE=res/raw/keystore.bks

echo | openssl s_client -connect $SERVER:443 2>&1 | \
 sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > mycert.pem


if [ -a $CERTSTORE ]; then
    rm $CERTSTORE || exit 1
fi

keytool \
      -importcert \
      -v \
      -trustcacerts \
      -alias 0 \
      -file mycert.pem \
      -keystore $CERTSTORE \
      -storetype BKS \
      -provider org.bouncycastle.jce.provider.BouncyCastleProvider \
      -providerpath ./ \
      -storepass $PASSWORD
