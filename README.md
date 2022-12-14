##**Notes**

###**notes.add** - создает новую заметку у текущего пользователя.

**Параметры:**

**title** (string) - заголовок заметки.

**text**(string) - текст заметки.

**privacy**(integer) - уровень доступа к заметке.

Возможные значения:

* 0 — все пользователи,
* 1 — только друзья,
* 2 — друзья и друзья друзей,
* 3 — только пользователь.

**commentPrivacy**(integer) - уровень доступа к комментированию заметки.

Возможные значения:

* 0 — все пользователи,
* 1 — только друзья,
* 2 — друзья и друзья друзей,
* 3 — только пользователь.

**Не реализовано:**

privacy_view(string) - настройки приватности просмотра заметки в специальном формате.

privacy_comment(string)  - настройки приватности комментирования заметки в специальном формате.

Результат - после успешного выполнения возвращает идентификатор созданной заметки (nid).

Коды ошибок:

В ходе выполнения могут произойти общие ошибки.

Связанные методы:

notes.edit - редактирует заметку текущего пользователя.

notes.delete - удаляет заметку текущего пользователя.

###**notes.createComment**

Добавляет новый комментарий к заметке.

(Этот метод можно вызвать с ключом доступа пользователя, полученным в Standalone-приложении через Implicit Flow. Требуются права доступа: notes. **Не реализовано**)

**Параметры:**

**note_id** - positive, идентификатор заметки.

**owner_id** - positive, идентификатор владельца заметки.

**reply_to** - positive, идентификатор пользователя, ответом на комментарий которого является добавляемый комментарий (не передаётся, если комментарий не является ответом).

**message(string)** - текст комментария.

**Не реализовано:**

guid(string) - уникальный идентификатор, предназначенный для предотвращения повторной отправки одинакового комментария.

Результат - после успешного выполнения возвращает идентификатор созданного комментария (cid).

Коды ошибок:

181 - Access to note denied

182 - You can't comment this note

В ходе выполнения могут произойти общие ошибки.

Связанные методы:

notes.editComment - редактирует указанный комментарий у заметки.

notes.deleteComment - удаляет комментарий к заметке.

###**notes.delete**

Удаляет заметку текущего пользователя.

**Параметры:**

**note_id** - positive, идентификатор заметки.

**Не реализовано:**

Результат - после успешного выполнения возвращает 1. 

Коды ошибок:

180 - Note not found

В ходе выполнения могут произойти общие ошибки.

Связанные методы:

notes.add - создает новую заметку у текущего пользователя.

notes.edit - редактирует заметку текущего пользователя.

###**notes.deleteComment**
Удаляет комментарий к заметке.

(Этот метод можно вызвать с ключом доступа пользователя, полученным в Standalone-приложении через Implicit Flow. Требуются права доступа: notes. **Не реализовано**)

**Параметры:**

**comment_id** - positive, идентификатор комментария.

**owner_id** - positive, идентификатор владельца заметки.

**Не реализовано:**

Результат - после успешного выполнения возвращает 1.

Коды ошибок:

181 - Access to note denied

183 - Access to comment denied

В ходе выполнения могут произойти общие ошибки.

Связанные методы:

notes.createComment - добавляет новый комментарий к заметке.

notes.restoreComment - восстанавливает удалённый комментарий.

##**notes.edit**

Редактирует заметку текущего пользователя.

**Параметры:**

**note_id** - positive, идентификатор заметки.

**title(string)** - заголовок заметки.

**text(string)** - текст заметки.

**privacy(integer)** - уровень доступа к заметке.

Возможные значения:

* 0 — все пользователи,
* 1 — только друзья,
* 2 — друзья и друзья друзей,
* 3 — только пользователь.

**comment_privacy(integer)** - уровень доступа к комментированию заметки.

Возможные значения:

* 0 — все пользователи,
* 1 — только друзья,
* 2 — друзья и друзья друзей,
* 3 — только пользователь.

**Не реализовано:**

privacy_view(string) - настройки приватности просмотра заметки в специальном формате.

privacy_comment(string) - настройки приватности комментирования заметки в специальном формате.

Результат - после успешного выполнения возвращает 1.

Коды ошибок:

180 - Note not found

В ходе выполнения могут произойти общие ошибки.

Связанные методы
notes.add - создает новую заметку у текущего пользователя.

notes.delete - удаляет заметку текущего пользователя.

##**notes.editComment**

Редактирует указанный комментарий у заметки.

(Этот метод можно вызвать с ключом доступа пользователя, полученным в Standalone-приложении через Implicit Flow. Требуются права доступа: notes. **Не реализовано**)

**Параметры:**

**comment_id** - positive, идентификатор комментария.

**owner_id** - positive, идентификатор владельца заметки.

**message(string)** - новый текст комментария.

**Не реализовано:**

Результат - после успешного выполнения возвращает 1.

Коды ошибок:

183 - Access to comment denied

В ходе выполнения могут произойти общие ошибки.

Связанные методы:

notes.createComment - добавляет новый комментарий к заметке.

##**notes.get**

Возвращает список заметок, созданных пользователем.

**Параметры:**

**note_ids(string)** - идентификаторы заметок, информацию о которых необходимо получить.

**user_id** - positive, идентификатор пользователя, информацию о заметках которого требуется получить.

**Не реализовано:**

offset - positive, смещение, необходимое для выборки определенного подмножества заметок.

count - positive, количество заметок, информацию о которых необходимо получить.

sort - positive, сортировка результатов (0 — по дате создания в порядке убывания, 1 - по дате создания в порядке возрастания).

Результат - после успешного выполнения возвращает список объектов заметок.

Коды ошибок:

180 - Note not found

В ходе выполнения могут произойти общие ошибки.

##**notes.getById**

Возвращает заметку по её id.

**Параметры:**

**note_id** - positive, идентификатор заметки.

**owner_id** - positive, идентификатор владельца заметки.

**Не реализовано:**

need_wiki - checkbox, определяет, требуется ли в ответе wiki-представление заметки (работает, только если запрашиваются заметки текущего пользователя).

Результат - после успешного выполнения возвращает список объектов заметок с дополнительными полями:

* privacy — уровень доступа к заметке (0 — все пользователи, 1 — только друзья, 2 — друзья и друзья друзей, 3 — только пользователь);

* comment_privacy — уровень доступа к комментированию заметки (0 — все пользователи, 1 — только друзья, 2 — друзья и друзья друзей, 3 — только пользователь);

* can_comment — может ли текущий пользователь комментировать заметку (1 — может, 0 — не может).

Коды ошибок:

180 - Note not found

181 - Access to note denied

В ходе выполнения могут произойти общие ошибки.

##**notes.getComments**

Возвращает список комментариев к заметке.

**Параметры:**

**note_id** - positive, идентификатор заметки.

**owner_id** - positive, идентификатор владельца заметки.

**sort** - positive, сортировка результатов (0 — по дате добавления в порядке возрастания, 1 — по дате добавления в порядке убывания).

**Не реализовано:**

offset - positive, смещение, необходимое для выборки определенного подмножества комментариев.

count - positive, количество комментариев, которое необходимо получить.

Результат  - возвращает массив объектов comment, каждый из которых содержит следующие поля:

* id — идентификатор комментария;
* uid — идентификатор автора комментария;
* nid — идентификатор заметки;
* oid — идентификатор владельца заметки;
* date — дата добавления комментария в формате unixtime;
* message — текст комментария;
* reply_to — идентификатор пользователя, в ответ на комментарий которого был оставлен текущий комментарий (если доступно).

Коды ошибок:

181 - Access to note denied

В ходе выполнения могут произойти общие ошибки.

Связанные методы:

newsfeed.getComments - возвращает данные, необходимые для показа раздела комментариев в новостях пользователя.

##**notes.getFriendsNotes**

Возвращает список заметок друзей пользователя.

Данный метод устарел и может быть отключён через некоторое время, пожалуйста, избегайте его использования.

Результат - после успешного выполнения возвращает список объектов заметок.

**Не реализовано:**

Параметры:

offset - positive, смещение, необходимое для выборки определенного подмножества заметок.

count - positive, количество заметок, информацию о которых необходимо получить.

Коды ошибок:

В ходе выполнения могут произойти общие ошибки.

##**notes.restoreComment**

Восстанавливает удалённый комментарий.

(Этот метод можно вызвать с ключом доступа пользователя, полученным в Standalone-приложении через Implicit Flow. Требуются права доступа: notes. **Не реализовано**)

**Параметры:**

**comment_id** - positive, идентификатор удаленного комментария.

**owner_id** - positive, идентификатор владельца заметки.

**Не реализовано:**

Результат - после успешного выполнения возвращает 1.

Коды ошибок:

183 - Access to comment denied

В ходе выполнения могут произойти общие ошибки.

Связанные методы:

notes.deleteComment - удаляет комментарий к заметке.
