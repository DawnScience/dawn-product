#!/bin/bash
#Input: must be run from the PRODUCT_ID/VERSION folder (see dViewer_pxsoft_installer.sh)
#Output: symbolic links for bin folder and operating systems

[ "${BASH_SOURCE[0]}" != "${0}" ] && THIS_SOURCED=1 || THIS_SOURCED=0

CURRENT_PATH=`pwd`

#Why removing and creating symbolic links again? Because if it exists, then it is created in dViewer* folder (=nonsense).
error=0
while true; do
  rm -f linux.i686
  ln -s `ls -d dViewer*-linux32` linux.i686
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f linux.x86_64
  ln -s `ls -d dViewer*-linux64` linux.x86_64
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f centos5-i686
  ln -s linux.i686 centos5-i686
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f centos5-x86_64
  ln -s linux.x86_64 centos5-x86_64
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f debian6-i686
  ln -s linux.i686 debian6-i686
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f debian6-x86_64
  ln -s linux.x86_64 debian6-x86_64
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f gentoo-i686
  ln -s linux.i686 gentoo-i686
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f gentoo-x86_64
  ln -s linux.x86_64 gentoo-x86_64
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f redhate5-i686
  ln -s linux.i686 redhate5-i686
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f redhate5-x86_64
  ln -s linux.x86_64 redhate5-x86_64
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f linux.i686/bin
  ln -s ../../generic/bin linux.i686/bin
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  rm -f linux.x86_64/bin
  ln -s ../../generic/bin linux.x86_64/bin
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  echo "Symbolic links are created successfully."
  break
done
cd ${CURRENT_PATH}
if [ ${error} -ne 0 ]; then
  echo "Error (${error}): creating of symbolic links failed"
fi
[[ "${THIS_SOURCED}" -ne 0 ]] && return ${error} || exit ${error}
