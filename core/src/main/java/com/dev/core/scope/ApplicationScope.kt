package com.dev.core.scope

import javax.inject.Scope

@Scope
@Retention
annotation class ApplicationScope

@Scope
@Retention
@Deprecated("don't use")
annotation class LegacyApplicationScope

@Scope
@Retention
annotation class MergedApplicationScope