package net.insprill.cjm.formatting

enum class FormatterType(
    val formatter: Formatter,
    val prettyName: String,
    val isCompatible: () -> (CompatibilityResult)
) {
    MINEDOWN(MineDownFormatter(), "MineDown", {
        CompatibilityResult(
            MineDownFormatter.isCompatible(),
            "You must be running 1.15.2+ to use the MineDown formatter!"
        )
    }),
    MINIMESSAGE(
        MiniMessageFormatter(),
        "MiniMessage",
        {
            CompatibilityResult(
                MiniMessageFormatter.isCompatible(),
                "You must be running Paper 1.18.2+ to use the MiniMessage formatter!"
            )
        }),
    LEGACY(LegacyFormatter(), "Legacy", { CompatibilityResult.NONE });

    data class CompatibilityResult(val status: Boolean, val message: String) {
        companion object {
            val NONE = CompatibilityResult(true, "")
        }
    }
}

