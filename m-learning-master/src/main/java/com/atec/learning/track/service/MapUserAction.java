package com.atec.learning.track.service;

import java.net.URLDecoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.common.web.BLCAbstractHandlerMapping;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * @author mahbouba
 *
 */

public class MapUserAction extends BLCAbstractHandlerMapping {

	private final String controllerName = "blProductController";

	@Value("${solr.index.use.sku}")
	protected boolean useSku;

	@Resource(name = "blCatalogService")
	private CatalogService catalogService;

	@Resource(name = "rdrUserMap")
	private UserMapUtils mapUtils;

	protected String defaultTemplateName = "catalog/product";

	public static final String CURRENT_PRODUCT_ATTRIBUTE_NAME = "currentProduct";

	public static final String BL_MAP_ACTION = "_bl_Map_action";

	@Value("${request.uri.encoding}")
	public String charEncoding;

	@Override
	protected Object getHandlerInternal(HttpServletRequest arg0)
			throws Exception {
		if (useSku) {
			return null;
		}
		BroadleafRequestContext context = BroadleafRequestContext
				.getBroadleafRequestContext();
		if (context != null && context.getRequestURIWithoutContext() != null) {
			String requestUri = URLDecoder.decode(
					context.getRequestURIWithoutContext(), charEncoding);
			Product product = catalogService.findProductByURI(requestUri);

			if (product != null) {
				String[] productIds = null;
				context.getRequest().setAttribute(
						CURRENT_PRODUCT_ATTRIBUTE_NAME, product);
				Map<String, Object> ruleMap = (Map<String, Object>) context
						.getWebRequest().getAttribute("blRuleMap",
								context.getWebRequest().SCOPE_REQUEST);

				Map<String, String> userActions = (Map<String, String>) ruleMap
						.get(BL_MAP_ACTION);

				mapUtils.MapSetting(userActions, productIds, product);
			}

		}

		return controllerName;
	}
}
