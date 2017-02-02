package com.dzaitsev.rxviper.plugin

import com.dzaitsev.rxviper.Router
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import rx.Observable
import rx.Observable.just
import javax.lang.model.element.Modifier

internal class RouterGenerator(feature: FeatureOptions) : Generator(feature) {
  override val typeName = "Router"

  override fun createSpec(): Observable<TypeSpec> {
    val routerBuilder = TypeSpec.interfaceBuilder(typeSpecName)
        .addSuperinterface(clazz<Router>())
        .addModifiers(Modifier.PUBLIC)
    feature.routesTo.forEach {
      routerBuilder.addMethod(MethodSpec.methodBuilder("navigateTo$it")
          .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
          .build())
    }
    return just(routerBuilder.build())
  }
}