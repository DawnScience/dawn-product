#!/bin/bash
#Input: must be run from the PRODUCT_ID/VERSION folder (see dViewer_pxsoft_installer.sh)
#Output: symbolic links for bin folder and operating systems

LINUX64_LINK="linux-x86_64"
LINUX32_LINK="linux-i686"

[ "${BASH_SOURCE[0]}" != "${0}" ] && THIS_SOURCED=1 || THIS_SOURCED=0

CURRENT_PATH="$(pwd)"

error=0
while true; do
  ln -fns `ls -d dViewer*-linux32` "${LINUX32_LINK}"
  error=${?}
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  ln -fns `ls -d dViewer*-linux64` "${LINUX64_LINK}"
  error=${?}
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  ln -fns ../../generic/bin "${LINUX32_LINK}"/bin
  error=${?}
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  ln -fns ../../generic/bin "${LINUX64_LINK}"/bin
  error=${?}
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic link in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  echo "Symbolic links are created successfully."
  break
done
cd "${CURRENT_PATH}"
if [ ${error} -ne 0 ]; then
  echo "Error (${error}): creating of symbolic links failed"
fi
[[ "${THIS_SOURCED}" -ne 0 ]] && return ${error} || exit ${error}
