package leviathan.CarPartsStore.controllers.views;

import leviathan.CarPartsStore.services.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeViewController {
    private final CatalogService catalogService;

    public HomeViewController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public ModelAndView showHomeView() {
        ModelAndView mav = new ModelAndView("home");
        mav.addObject("top4Catalogs", catalogService.getTop4ActiveByPopularity());
        return mav;
    }
}
