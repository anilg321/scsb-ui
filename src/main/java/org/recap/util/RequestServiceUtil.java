package org.recap.util;

import org.apache.commons.lang3.StringUtils;
import org.recap.RecapConstants;
import org.recap.model.jpa.RequestItemEntity;
import org.recap.model.search.RequestForm;
import org.recap.repository.jpa.RequestItemDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by rajeshbabuk on 29/10/16.
 */
@Service
public class RequestServiceUtil {

    @Autowired
    RequestItemDetailsRepository requestItemDetailsRepository;

    public Page<RequestItemEntity> searchRequests(RequestForm requestForm) {
        String patronBarcode = StringUtils.isNotBlank(requestForm.getPatronBarcode()) ? requestForm.getPatronBarcode().trim() : requestForm.getPatronBarcode();
        String itemBarcode = StringUtils.isNotBlank(requestForm.getItemBarcode()) ? requestForm.getItemBarcode().trim() : requestForm.getItemBarcode();
        String status = StringUtils.isNotBlank(requestForm.getStatus()) ? requestForm.getStatus().trim() : requestForm.getStatus();
        Pageable pageable = new PageRequest(requestForm.getPageNumber(), requestForm.getPageSize(), Sort.Direction.DESC, RecapConstants.REQUEST_ID);

        Page<RequestItemEntity> requestItemEntities;
        if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isNotBlank(status)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndItemBarcodeAndActive(pageable, patronBarcode, itemBarcode);
            } else {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndItemBarcodeAndStatus(pageable, patronBarcode, itemBarcode, status);
            }
        } else if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isBlank(status)) {
            requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndItemBarcode(pageable, patronBarcode, itemBarcode);
        } else if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isBlank(itemBarcode) && StringUtils.isNotBlank(status)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndActive(pageable, patronBarcode);
            } else {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndStatus(pageable, patronBarcode, status);
            }
        } else if (StringUtils.isBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isNotBlank(status)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findByItemBarcodeAndActive(pageable, itemBarcode);
            } else {
                requestItemEntities = requestItemDetailsRepository.findByItemBarcodeAndStatus(pageable, itemBarcode, status);
            }
        } else if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isBlank(itemBarcode) && StringUtils.isBlank(status)) {
            requestItemEntities = requestItemDetailsRepository.findByPatronBarcode(pageable, patronBarcode);
        } else if (StringUtils.isBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isBlank(status)) {
            requestItemEntities = requestItemDetailsRepository.findByItemBarcode(pageable, itemBarcode);
        } else if (StringUtils.isBlank(patronBarcode) && StringUtils.isBlank(itemBarcode) && StringUtils.isNotBlank(status)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findAllActive(pageable);
            } else {
                requestItemEntities = requestItemDetailsRepository.findByStatus(pageable, status);
            }
        } else {
            requestItemEntities = requestItemDetailsRepository.findAllActive(pageable);
        }
        return requestItemEntities;
    }
}
