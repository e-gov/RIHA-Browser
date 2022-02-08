package ee.ria.riha.web;

import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.service.UserService;
import ee.ria.riha.service.util.*;
import ee.ria.riha.web.model.UserDetailsModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

@RestController
@RequestMapping(API_V1_PREFIX + "/my/organization")
@Api("Organization")
@Slf4j
public class OrganizationController {

	private static final String SORT_DELIMITER = "-";
	static final Map<String, Comparator<UserDetailsModel>> USER_DETAILS_COMPARATORS;

	static {
		USER_DETAILS_COMPARATORS = new HashMap<>();
		USER_DETAILS_COMPARATORS.put("firstName", Comparator.comparing(UserDetailsModel::getFirstName, Comparator.nullsLast(String::compareToIgnoreCase)));
		USER_DETAILS_COMPARATORS.put("lastName", Comparator.comparing(UserDetailsModel::getLastName, Comparator.nullsLast(String::compareToIgnoreCase)));
		USER_DETAILS_COMPARATORS.put("email", Comparator.comparing(UserDetailsModel::getEmail, Comparator.nullsLast(String::compareToIgnoreCase)));
		USER_DETAILS_COMPARATORS.put("approver", Comparator.comparing(UserDetailsModel::getApprover, Comparator.nullsLast(Boolean::compareTo)));
		USER_DETAILS_COMPARATORS.put("producer", Comparator.comparing(UserDetailsModel::getProducer, Comparator.nullsLast(Boolean::compareTo)));
	}


	@Autowired
	private UserService userService;

	@GetMapping("/users")
	@PreAuthorize("hasRole('ROLE_RIHA_USER')")
	@ApiOperation("List users of given organization")
	@ApiPageableAndCompositeRequestParams
	public ResponseEntity<PagedResponse<UserDetailsModel>> listOrganizationUsers(HttpServletRequest request, Pageable pageable, CompositeFilterRequest filterRequest) {
		if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof RihaUserDetails)) {
			log.error("principal is not of RihaUserDetail type, but instead {}", SecurityContextHolder.getContext().getAuthentication().getClass().getCanonicalName());
			return ResponseEntity.badRequest().build();
		}
		RihaUserDetails rihaUserDetails = (RihaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<UserDetailsModel> users = userService.getUsersByOrganization(rihaUserDetails.getActiveOrganization().getCode());

		int totalUsers = users.size();
		String sortParameter = getSortFieldFromFilterRequest(filterRequest);
		Comparator<UserDetailsModel> sortFunction =
				sortParameter != null
						? USER_DETAILS_COMPARATORS.get(sortParameter.replace(SORT_DELIMITER, ""))
						: null;
		if (sortParameter != null && sortFunction != null) {
			sortUsers(users, sortFunction, sortParameter.startsWith(SORT_DELIMITER));
		}

		users = applyPaging(pageable, users);

		return ResponseEntity.ok(new PagedResponse<>(
				new PageRequest(pageable.getPageNumber(), pageable.getPageSize()),
				totalUsers,
				users));
	}

	private String getSortFieldFromFilterRequest(CompositeFilterRequest filterRequest) {
		return filterRequest != null && filterRequest.getSortParameters() != null && !filterRequest.getSortParameters().isEmpty()
				? filterRequest.getSortParameters().get(0)
				: null;
	}

	static void sortUsers(List<UserDetailsModel> users, Comparator<UserDetailsModel> sortFunction, boolean reverseSort) {
		users.sort(reverseSort ? sortFunction.reversed() : sortFunction);
	}

	private List<UserDetailsModel> applyPaging(Pageable pageable, List<UserDetailsModel> users) {
		int offset = pageable.getOffset();
		return users.subList(Math.min(users.size(), offset), Math.min(users.size(), offset + pageable.getPageSize()));
	}
}
