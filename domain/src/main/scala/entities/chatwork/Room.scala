package org.nisshiee.chatwork_lambda_test.domain.chatwork

import monocle.macros.Lenses
import org.nisshiee.chatwork_lambda_test.domain._

@Lenses case class Room(
  id: Id[Room],
  name: String,
  iconPath: String)
