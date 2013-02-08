package com.jayway.rps;

import com.jayway.rps.structs.Command;

public interface ApplicationService {
	void handle(Command command);
}

