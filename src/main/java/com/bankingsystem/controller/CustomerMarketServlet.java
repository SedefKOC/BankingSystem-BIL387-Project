package com.bankingsystem.controller;

import com.bankingsystem.entity.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class CustomerMarketServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAuthorized(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        String displayName = CustomerAccountsServlet.CustomerViewHelper.displayName(session);
        req.setAttribute("homeUsername", CustomerAccountsServlet.CustomerViewHelper.sanitize(displayName));
        req.setAttribute("homeInitials", CustomerAccountsServlet.CustomerViewHelper.initials(displayName));
        req.setAttribute("navActive", "market");
        req.setAttribute("currencyRates", sampleCurrencies());
        req.setAttribute("commodityRates", sampleCommodities());
        req.setAttribute("marketNews", sampleNews());
        req.getRequestDispatcher("/WEB-INF/views/customer-market.jsp").forward(req, resp);
    }

    private boolean isAuthorized(HttpSession session) {
        return session != null && session.getAttribute("currentUserRole") == UserRole.CUSTOMER;
    }

    private List<MarketRow> sampleCurrencies() {
        return List.of(
                new MarketRow("USD / TRY", "34.5842", "+0.45%"),
                new MarketRow("EUR / TRY", "36.4015", "+0.22%"),
                new MarketRow("GBP / TRY", "42.1021", "-0.12%"),
                new MarketRow("JPY / TRY", "0.2187", "+0.35%")
        );
    }

    private List<MarketRow> sampleCommodities() {
        return List.of(
                new MarketRow("Gram Altın", "2,954.50 ₺", "+0.18%"),
                new MarketRow("ONS Altın", "2,420.12 $", "-0.05%"),
                new MarketRow("Petrol (Brent)", "81.40 $", "+1.22%"),
                new MarketRow("BIST 100", "9,515", "+0.65%")
        );
    }

    private List<String> sampleNews() {
        return List.of(
                "TCMB haftalık repo ihalesiyle piyasaya 120 milyar TL verdi.",
                "ABD enflasyon verisi beklentinin altında gelerek dolar endeksini düşürdü.",
                "Avrupa Merkez Bankası faiz indirimi sinyali verdi.",
                "Altın fiyatları jeopolitik gerilimle yeniden yükselişe geçti."
        );
    }

    public record MarketRow(String name, String value, String change) { }
}
