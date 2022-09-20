rootProject.name = "Doctor"
include("download")
include("command")
include("executor")
include("command:fakes")
findProject(":command:fakes")?.name = "fakes"
include("models")
include("assignment")
include("utils")
include("db")
include("models")
include("sources")
include("utils:routing")
findProject(":utils:routing")?.name = "routing"
include("assignment:presenters")
findProject(":assignment:presenters")?.name = "presenters"
include("assignment:backend")
findProject(":assignment:backend")?.name = "backend"
include("assignment:api")
findProject(":assignment:api")?.name = "api"
include("utils")
include("download:api")
findProject(":download:api")?.name = "api"
include("download:backend")
findProject(":download:backend")?.name = "backend"
include("download:presenters")
findProject(":download:presenters")?.name = "presenters"
include("assignment:backend:fakes")
findProject(":assignment:backend:fakes")?.name = "fakes"
include("sources:fakes")
findProject(":sources:fakes")?.name = "fakes"
include("executor:fakes")
findProject(":executor:fakes")?.name = "fakes"
