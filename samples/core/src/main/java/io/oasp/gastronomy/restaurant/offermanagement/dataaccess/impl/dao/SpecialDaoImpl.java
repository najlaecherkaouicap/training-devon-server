package io.oasp.gastronomy.restaurant.offermanagement.dataaccess.impl.dao;

import javax.inject.Named;

import com.mysema.query.alias.Alias;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.EntityPathBase;

import io.oasp.gastronomy.restaurant.general.common.api.datatype.Money;
import io.oasp.gastronomy.restaurant.general.dataaccess.base.dao.ApplicationDaoImpl;
import io.oasp.gastronomy.restaurant.offermanagement.common.api.datatype.DayOfWeek;
import io.oasp.gastronomy.restaurant.offermanagement.dataaccess.api.SpecialEntity;
import io.oasp.gastronomy.restaurant.offermanagement.dataaccess.api.dao.SpecialDao;
import io.oasp.gastronomy.restaurant.offermanagement.logic.api.to.SpecialSearchCriteriaTo;
import io.oasp.gastronomy.restaurant.offermanagement.logic.api.to.WeeklyPeriodSearchCriteriaTo;
import io.oasp.module.jpa.common.api.to.PaginatedListTo;

/**
 * This is the implementation of {@link SpecialDao}.
 */
@Named
public class SpecialDaoImpl extends ApplicationDaoImpl<SpecialEntity> implements SpecialDao {

  /**
   * The constructor.
   */
  public SpecialDaoImpl() {

    super();
  }

  @Override
  public Class<SpecialEntity> getEntityClass() {

    return SpecialEntity.class;
  }

  @Override
  public PaginatedListTo<SpecialEntity> findSpecials(SpecialSearchCriteriaTo criteria) {

    SpecialEntity special = Alias.alias(SpecialEntity.class);
    EntityPathBase<SpecialEntity> alias = Alias.$(special);
    JPAQuery query = new JPAQuery(getEntityManager()).from(alias);

    String name = criteria.getName();
    if (name != null) {
      query.where(Alias.$(special.getName()).eq(name));
    }

    Long offer = criteria.getOfferId();
    if (offer != null) {
      query.where(Alias.$(special.getOfferId()).eq(offer));
    }
    /*
     * d) In addition you will get a compiler error in SpecialDaoImpl#findSpecials. This is caused by generating DAOs
     * with embedded entities as dependencies, which is currently not handled automatically by CobiGen. Fix it
     * appropriately by creating a where clause for each field provided by the WeeklyPeriodSearchCriteria!
     */
    /*
     * private DayOfWeek startingDay;
     *
     * private int startingHour;
     *
     * private DayOfWeek endingDay;
     *
     * private int endingHour;
     */
    WeeklyPeriodSearchCriteriaTo activePeriodCriteria = criteria.getActivePeriod();

    DayOfWeek startingDay = activePeriodCriteria.getStartingDay();
    //////////////// EXERCICE 3 OF THE TRAINING///////////////////
    if (startingDay != null) {
      query.where(Alias.$(special.getActivePeriod().getStartingDay()).eq(startingDay));
    }

    /////
    int startingHour = activePeriodCriteria.getStartingHour();

    if (startingHour != 0) {
      query.where(Alias.$(special.getActivePeriod().getStartingHour()).eq(startingHour));
    }
    /////
    DayOfWeek endingDay = activePeriodCriteria.getEndingDay();

    if (endingDay != null) {
      query.where(Alias.$(special.getActivePeriod().getStartingDay()).eq(endingDay));
    }
    ////////

    int endingHour = activePeriodCriteria.getStartingHour();

    if (startingHour != 0) {
      query.where(Alias.$(special.getActivePeriod().getEndingHour()).eq(endingHour));
    }

    ////////////////////////////////////
    Money specialPrice = criteria.getSpecialPrice();
    if (specialPrice != null) {
      query.where(Alias.$(special.getSpecialPrice()).eq(specialPrice));
    }

    return findPaginated(criteria, query, alias);
  }

}
