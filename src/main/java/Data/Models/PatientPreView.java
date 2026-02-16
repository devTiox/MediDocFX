package Data.Models;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public record PatientPreView(
        int id,
        String lastName,
        String name,
        String phoneNumber
) {
    private static final Collator POLISH_COLLATOR =
            Collator.getInstance(Locale.of("pl", "PL"));

    static {
        POLISH_COLLATOR.setStrength(Collator.PRIMARY);
    }

    public static final Comparator<PatientPreView> BY_LN_N_ID =
            Comparator
                    .comparing(PatientPreView::lastName, POLISH_COLLATOR)
                    .thenComparing(PatientPreView::name, POLISH_COLLATOR)
                    .thenComparing(PatientPreView::id);
}
