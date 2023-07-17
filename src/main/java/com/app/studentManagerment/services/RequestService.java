package com.app.studentManagerment.services;

import com.app.studentManagerment.entity.Requests;
import com.app.studentManagerment.enumPack.enumStatus;

public interface RequestService {
	Requests findRequestWithDescrip(String description);

	Requests addRequest(String description);

	void changeStatus(Requests requests, enumStatus processing);
}
