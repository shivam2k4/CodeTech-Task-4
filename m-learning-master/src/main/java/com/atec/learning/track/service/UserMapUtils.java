package com.atec.learning.track.service;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.springframework.stereotype.Component;

import com.atec.learning.track.domain.type.RdrUserAction;
import com.atec.learning.track.exceptions.UserTrackExceptions;


/**
 * 
 * @author mahbouba
 *
 */
@Component("rdrUserMap")
public class UserMapUtils {

	public static final String BL_MAP_ACTION = "_bl_Map_action";

	@Resource(name = "rdrUserTrackService")
	protected UserTrackService trackService;

	private static final Log LOG = LogFactory
			.getLog(UserTrackServiceImpl.class);

	@SuppressWarnings("unchecked")
	protected void MapSetting(Map<String, String> userActions,
			String[] productIds, Product product) throws UserTrackExceptions

	{

		if (userActions != null) {

			// View product
			if (userActions.get(RdrUserAction.VIEW.getType()) != null) {
				productIds = userActions.get(RdrUserAction.VIEW.getType())
						.split(",");

				if (!checkProductAction(productIds, product)) {
					ArrayUtils.add(productIds, product.getId());
					userActions.put(RdrUserAction.VIEW.getType(),
							productIds.toString());
				}

			}

			// Like product
			if (userActions.get(RdrUserAction.LIKE.getType()) != null) {
				productIds = userActions.get(RdrUserAction.LIKE.getType())
						.split(",");

				if (!checkProductAction(productIds, product)) {
					ArrayUtils.add(productIds, product.getId());

					userActions.put(RdrUserAction.LIKE.getType(),
							productIds.toString());
				}
			}
		}

		// Rate product
		if (userActions.get(RdrUserAction.RATE.getType()) != null) {
			productIds = userActions.get(RdrUserAction.RATE.getType()).split(
					",");

			if (!checkProductAction(productIds, product)) {
				ArrayUtils.add(productIds, product.getId());

				userActions.put(RdrUserAction.VIEW.getType(),
						productIds.toString());
			}
		}

	}

	/**
	 * 
	 * @param cette
	 *            methode permet de verifier l'existance d'un produit dans la
	 *            liste de la map
	 * @param
	 * @return boolean
	 */

	public boolean checkProductAction(String[] productIds, Product product) {
		boolean exist = false;
		// verifier si la map n'a pas vide
		if (!ArrayUtils.isEmpty(productIds)) {

			// parcours de la liste
			for (String id : productIds) {
				// verification de l'existance d'un produit entré dans le
				// contenu de la liste
				Long productId = Long.parseLong(id);
				if ((product.getId() == productId)) {
					exist = true;
					break;
				}
			}

		}
		return exist;
	}

}
