FasdUAS 1.101.10   ��   ��    k             l     ��������  ��  ��     ��  i       	  I     �� 
��
�� .aevtoappnull  �   � **** 
 o      ���� 0 argv  ��   	 k     �       l     ��������  ��  ��        r         n         4    �� 
�� 
cobj  m    ����   o     ���� 0 argv    o      ���� 0 process_path        r        n        4    �� 
�� 
cobj  m   	 
����   o    ���� 0 argv    o      ���� 0 filename        l   ��������  ��  ��        Z    �   !����   =    " # " n     $ % $ 1    ��
�� 
prun % m     & &�                                                                                  rimZ  alis    >  Macintosh HD                   BD ����Google Chrome.app                                              ����            ����  
 cu             Applications  !/:Applications:Google Chrome.app/   $  G o o g l e   C h r o m e . a p p    M a c i n t o s h   H D  Applications/Google Chrome.app  / ��   # m    ��
�� boovtrue ! O    � ' ( ' k    � ) )  * + * r    ! , - , n     . / . m    ��
�� 
nmbr / 2   ��
�� 
cwin - o      ���� 0 windowcount windowCount +  0�� 0 Y   " � 1�� 2 3�� 1 k   , � 4 4  5 6 5 r   , 6 7 8 7 n   , 4 9 : 9 m   2 4��
�� 
nmbr : n  , 2 ; < ; 2  0 2��
�� 
CrTb < 4   , 0�� =
�� 
cwin = o   . /���� 0 x   8 o      ���� 0 tabcount tabCount 6  >�� > Y   7 � ?�� @ A�� ? k   A � B B  C D C r   A J E F E n   A H G H G 4   E H�� I
�� 
CrTb I o   F G���� 0 y   H 4   A E�� J
�� 
cwin J o   C D���� 0 x   F o      ���� 0 thistab   D  K L K r   K R M N M n   K P O P O 1   N P��
�� 
strq P n   K N Q R Q 1   L N��
�� 
URL  R o   K L���� 0 thistab   N o      ���� 
0 taburl   L  S T S I  S d�� U��
�� .sysodlogaskr        TEXT U b   S ` V W V b   S ^ X Y X b   S Z Z [ Z b   S X \ ] \ b   S V ^ _ ^ m   S T ` ` � a a  p y t h o n 2 . 7   _ o   T U���� 0 process_path   ] m   V W b b � c c  t a b k i l l e r . p y   [ o   X Y���� 
0 taburl   Y m   Z ] d d � e e    W o   ^ _���� 0 filename  ��   T  f g f O  e � h i h r   k � j k j I  k ��� l��
�� .sysoexecTEXT���     TEXT l l  k | m���� m b   k | n o n b   k z p q p b   k v r s r b   k t t u t b   k p v w v m   k n x x � y y  p y t h o n 2 . 7   w o   n o���� 0 process_path   u m   p s z z � { {  t a b k i l l e r . p y   s o   t u���� 
0 taburl   q m   v y | | � } }    o o   z {���� 0 filename  ��  ��  ��   k o      ���� 
0 status   i m   e h��
�� misccura g  ~�� ~ Z  � �  �����  =  � � � � � o   � ����� 
0 status   � m   � � � � � � �  T r u e � I  � ��� ���
�� .coredelonull���     obj  � o   � ����� 0 thistab  ��  ��  ��  ��  �� 0 y   @ m   : ;����  A o   ; <���� 0 tabcount tabCount��  ��  �� 0 x   2 m   % &����  3 o   & '���� 0 windowcount windowCount��  ��   ( m     � ��                                                                                  rimZ  alis    >  Macintosh HD                   BD ����Google Chrome.app                                              ����            ����  
 cu             Applications  !/:Applications:Google Chrome.app/   $  G o o g l e   C h r o m e . a p p    M a c i n t o s h   H D  Applications/Google Chrome.app  / ��  ��  ��     � � � l  � ��� � ���   � l ftell current application to do shell script ("python2.7 " & pathname & "processkiller.py " & filename)    � � � � � t e l l   c u r r e n t   a p p l i c a t i o n   t o   d o   s h e l l   s c r i p t   ( " p y t h o n 2 . 7   "   &   p a t h n a m e   &   " p r o c e s s k i l l e r . p y   "   &   f i l e n a m e ) �  � � � l  � ���������  ��  ��   �  � � � l   � ��� � ���   �#if application "Safari" is running then		tell application "Safari"			set windowCount to number of windows			repeat with x from 1 to windowCount				set tabCount to number of tabs in window x				repeat with y from 1 to tabCount					set thistab to tab y of window x					set taburl to URL of thistab					tell current application to set status to do shell script ("python2.7 " & process_path & "tabkiller.py " & taburl & " " & filename)					if status is equal to "True" then delete thistab				end repeat			end repeat		end tell	end if    � � � �: i f   a p p l i c a t i o n   " S a f a r i "   i s   r u n n i n g   t h e n  	 	 t e l l   a p p l i c a t i o n   " S a f a r i "  	 	 	 s e t   w i n d o w C o u n t   t o   n u m b e r   o f   w i n d o w s  	 	 	 r e p e a t   w i t h   x   f r o m   1   t o   w i n d o w C o u n t  	 	 	 	 s e t   t a b C o u n t   t o   n u m b e r   o f   t a b s   i n   w i n d o w   x  	 	 	 	 r e p e a t   w i t h   y   f r o m   1   t o   t a b C o u n t  	 	 	 	 	 s e t   t h i s t a b   t o   t a b   y   o f   w i n d o w   x  	 	 	 	 	 s e t   t a b u r l   t o   U R L   o f   t h i s t a b  	 	 	 	 	 t e l l   c u r r e n t   a p p l i c a t i o n   t o   s e t   s t a t u s   t o   d o   s h e l l   s c r i p t   ( " p y t h o n 2 . 7   "   &   p r o c e s s _ p a t h   &   " t a b k i l l e r . p y   "   &   t a b u r l   &   "   "   &   f i l e n a m e )  	 	 	 	 	 i f   s t a t u s   i s   e q u a l   t o   " T r u e "   t h e n   d e l e t e   t h i s t a b  	 	 	 	 e n d   r e p e a t  	 	 	 e n d   r e p e a t  	 	 e n d   t e l l  	 e n d   i f �  � � � l  � ���������  ��  ��   �  � � � l  � ���������  ��  ��   �  � � � l  � ���������  ��  ��   �  � � � l  � ���������  ��  ��   �  ��� � l  � ���������  ��  ��  ��  ��       �� � ���   � ��
�� .aevtoappnull  �   � **** � �� 	���� � ���
�� .aevtoappnull  �   � ****�� 0 argv  ��   � �������� 0 argv  �� 0 x  �� 0 y   � ������ &�������������������� ` b d���� x z |���� ���
�� 
cobj�� 0 process_path  �� 0 filename  
�� 
prun
�� 
cwin
�� 
nmbr�� 0 windowcount windowCount
�� 
CrTb�� 0 tabcount tabCount�� 0 thistab  
�� 
URL 
�� 
strq�� 
0 taburl  
�� .sysodlogaskr        TEXT
�� misccura
�� .sysoexecTEXT���     TEXT�� 
0 status  
�� .coredelonull���     obj �� ���k/E�O��l/E�O��,e  �� �*�-�,E�O �k�kh *�/�-�,E�O fk�kh *�/�/E�O��,�,E�O��%�%�%a %�%j Oa  a �%a %�%a %�%j E` UO_ a   
�j Y h[OY��[OY��UY hOPascr  ��ޭ