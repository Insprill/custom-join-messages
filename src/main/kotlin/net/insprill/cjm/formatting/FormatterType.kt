package net.insprill.cjm.formatting

@Suppress("unused")
enum class FormatterType(
    val formatter: Formatter,
    val prettyName: String,
    val isCompatible: () -> (CompatibilityResult)
) {
    MINEDOWN(MinedownFormatter(), "Minedown", { CompatibilityResult.NONE }),
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

