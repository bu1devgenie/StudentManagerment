package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.RequestRepository;
import com.app.studentManagerment.entity.Requests;
import com.app.studentManagerment.enumPack.enumStatus;
import com.app.studentManagerment.services.RequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RequestServiceImpl implements RequestService {
	private RequestRepository requestRepository;

	public RequestServiceImpl(RequestRepository requestRepository) {
		this.requestRepository = requestRepository;
	}

	@Override
	public Requests findRequestWithDescrip(String description) {
		return requestRepository.findRequestsByDescription(description);
	}

	@Override
	public Requests addRequest(String description) {
		Requests request = new Requests();
		request.setTime(LocalDateTime.now());
		request.setDescription(description);
		return requestRepository.save(request);
	}

	@Override
	public void changeStatus(Requests requests, enumStatus processing) {
		if (requests != null) {
			requests.setStatus(processing);
		}
	}
}
