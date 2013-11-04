#!/bin/bash
#Input: $1 as BUCKMINSTER_WORKSPACE_PATH, $2 as PRODUCT_ID, $3 as VERSION
#Output: installed product(s) into pxsoft

[ "${BASH_SOURCE[0]}" != "${0}" ] && THIS_SOURCED=1 || THIS_SOURCED=0

LINK_CREATOR_FILE="dViewer_create_links.sh"

CURRENT_PATH=`pwd`

#Give group write right to pxsoft members
umask g+w
error=0
while true; do
  BUCKMINSTER_WORKSPACE_PATH="${1}"
  PRODUCT_ID="${2}"
  VERSION="${3}"
  if [ -z "${1}" ] || [ ! -d "${1}" ] || [ -z "${2}" ] || [ -z "${3}" ]; then
    error=1
    echo "Usage: ${0} <buckminster workspace folder> <product id> <product version>"
    echo "buckminster workspace folder: it must exist, and it must contain the built products."
    echo "product id: name of product (currently dViewer)."
    echo "product version: version of product (usually v<YearMoDa>, where Year, Mo(nth), Da(y) forms a date)."
    echo "For example: ${0} /scisoft/pxsoft/src/dViewer/vmaster dViewer v20131031"
    break
  fi
  source .get_pxsoft_target_root
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not query pxsoft target folder"
    break
  fi
  LINK_CREATOR_FOLDER_PATH="${pxsoft_target}/${PRODUCT_ID}/generic"
  LINK_CREATOR_FILE_PATH="${LINK_CREATOR_FOLDER_PATH}/${LINK_CREATOR_FILE}"
  PRODUCT_TARGET_PATH="${pxsoft_target}/${PRODUCT_ID}/${VERSION}"
  if [ -d "${PRODUCT_TARGET_PATH}" ]; then
    echo "Cleaning ${PRODUCT_TARGET_PATH} folder."
    rm -rf "${PRODUCT_TARGET_PATH}/"*
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not delete content of ${PRODUCT_TARGET_PATH} folder"
      break
    fi
  else
    echo "Creating ${PRODUCT_TARGET_PATH} folder."
    mkdir -p "${PRODUCT_TARGET_PATH}"
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not make ${PRODUCT_TARGET_PATH} folder"
      break
    fi
  fi
  #Changing directory to product target
  cd "${PRODUCT_TARGET_PATH}"
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not change to ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  cur_path=`pwd`
  #Copy the compressed products to pxsoft and extract them
  echo "Copying compressed products to pxsoft, this can take a while."
  cp -pf "${BUCKMINSTER_WORKSPACE_PATH}/products/${PRODUCT_ID}"*.zip ./
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not copy compressed products to ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  echo "Extracting compressed products in pxsoft, this can take a while:"
  ls *.zip
  unzip '*.zip'
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not extract compressed products in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  #Copying ${LINK_CREATOR_FILE} from builder to pxsoft
  echo "Copying symbolic link creator to pxsoft."
  cp -pf "${BUCKMINSTER_WORKSPACE_PATH}/${LINK_CREATOR_FILE}" "${LINK_CREATOR_FOLDER_PATH}"
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not copy compressed products to ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  echo "Creating symbolic links for operating systems."
  ${LINK_CREATOR_FILE_PATH}
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not create symbolic links for operating systems in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  echo "Deleting compressed products."
  rm -f "${PRODUCT_TARGET_PATH}/"*.zip
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not delete compressed products in ${PRODUCT_TARGET_PATH} folder"
    break
  fi
  echo "${PRODUCT_ID} is installed in ${PRODUCT_TARGET_PATH} succesfully."
  break
done
if [ ${error} -ne 0 ]; then
  echo "Error (${error}): pxsoft installer failed"
fi
cd ${CURRENT_PATH}
[[ "${THIS_SOURCED}" -ne 0 ]] && return ${error} || exit ${error}
