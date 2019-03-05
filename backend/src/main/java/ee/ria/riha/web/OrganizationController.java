package ee.ria.riha.web;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.ria.riha.service.UserService;
import ee.ria.riha.storage.util.ApiPageableAndCompositeRequestParams;
import ee.ria.riha.storage.util.CompositeFilterRequest;
import ee.ria.riha.storage.util.PageRequest;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import ee.ria.riha.web.model.UserDetailsModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

@RestController
@RequestMapping(API_V1_PREFIX + "/my/organization")
@Api("Organization")
@Slf4j
public class OrganizationController {

	private static final String SORT_DELIMITER = "-";
	private static final Map<String, Function<UserDetailsModel, ? extends Comparable>> sortFunctions;
	static {
		sortFunctions = new HashMap<>();
		sortFunctions.put("firstName", UserDetailsModel::getFirstName);
		sortFunctions.put("lastName", UserDetailsModel::getLastName);
		sortFunctions.put("email", UserDetailsModel::getEmail);
		sortFunctions.put("approver", UserDetailsModel::getApprover);
		sortFunctions.put("producer", UserDetailsModel::getProducer);
	}

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	@PreAuthorize("hasRole('ROLE_RIHA_USER')")
	@ApiOperation("List users of given organization")
	@ApiPageableAndCompositeRequestParams
	public ResponseEntity<PagedResponse<UserDetailsModel>> listOrganizationUsers(HttpServletRequest request, Pageable pageable, CompositeFilterRequest filterRequest) {
		String organizationCode = request.getParameter("organizationCode");
		List<UserDetailsModel> users = userService.getUsersByOrganization(organizationCode);

		int totalUsers = users.size();
		sortUsers(filterRequest, users);
		users = applyPaging(pageable, users);

		return ResponseEntity.ok(new PagedResponse(
				new PageRequest(pageable.getPageNumber(), pageable.getPageSize()),
				totalUsers,
				users));
	}

	private void sortUsers(CompositeFilterRequest filterRequest, List<UserDetailsModel> users) {
		List<String> sortParameters = filterRequest.getSortParameters();
		if (!sortParameters.isEmpty()) {
			String sort = sortParameters.get(0);
			Function<UserDetailsModel, ? extends Comparable> sortFunction = sortFunctions.get(sort.replace(SORT_DELIMITER, ""));
			if (sortFunction == null) {
				return;
			}
			Comparator<UserDetailsModel> comparator = Comparator.comparing(sortFunction);
			users.sort(sort.startsWith(SORT_DELIMITER) ? comparator.reversed() : comparator);
		}
	}

	private List<UserDetailsModel> applyPaging(Pageable pageable, List<UserDetailsModel> users) {
		int offset = pageable.getOffset();
		return users.subList(Math.min(users.size(), offset), Math.min(users.size(), offset + pageable.getPageSize()));
	}
}
