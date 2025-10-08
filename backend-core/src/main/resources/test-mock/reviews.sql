SET search_path = app;

INSERT INTO review (media_id, username, title, content, spoiler_free, modified_at, created_at)
values ((SELECT id FROM media WHERE title = 'Dune: Part One'),
        'user7',
        'Great and well directed Adventure/Drama',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam odio quam, fringilla eu consectetur eget, lacinia rutrum odio. Sed lobortis elementum congue. Cras at massa dolor. Morbi metus ex, euismod sit amet leo semper, elementum pharetra nisl. Curabitur pretium risus non nisi tristique, vitae finibus tellus pretium. Duis eu ultrices ante. Praesent massa ex, volutpat nec magna ac, vehicula sollicitudin quam. Integer vel est eget sapien finibus dignissim. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.',
        false,
        now() + make_interval(secs := 1),
        now() + make_interval(secs := 1));

INSERT INTO review (media_id, username, title, content, spoiler_free, modified_at, created_at)
values ((SELECT id FROM media WHERE title = 'Dune: Part One'),
        'user8',
        'This is only the beginning!',
        'Cras lacinia ante at eros efficitur feugiat. In dapibus ut augue non laoreet. Etiam pharetra odio a magna placerat eleifend. Aenean et diam ac purus varius sodales ut vitae nulla. Cras consectetur pharetra tellus. Ut sodales elementum est et varius. In tincidunt lobortis urna eget feugiat. Phasellus sit amet mauris at metus cursus condimentum. Maecenas tristique odio nunc, vestibulum consequat ante auctor eu. Morbi id posuere orci. Quisque tempus lacinia nibh eget molestie. Pellentesque tristique bibendum consectetur. Pellentesque aliquet aliquet porttitor. Suspendisse interdum lacus sit amet velit egestas pellentesque.',
        false,
        now() + make_interval(secs := 2),
        now() + make_interval(secs := 2));

INSERT INTO review (media_id, username, title, content, spoiler_free, modified_at, created_at)
values ((SELECT id FROM media WHERE title = 'Dune: Part One'),
        'user9',
        'Visually stunning but mostly about setting the stage',
        'Donec eget justo molestie, suscipit dolor sed, varius ex. In suscipit accumsan nulla, vel interdum quam egestas eget. Sed dui quam, euismod sed nisl eu, luctus volutpat arcu. Sed vitae mi in dui tempor vestibulum laoreet luctus enim. Pellentesque accumsan, nisi sit amet aliquam fringilla, libero metus commodo sapien, ac ultricies metus ante pretium urna. Morbi nec mi a tortor sollicitudin gravida. Cras et purus vestibulum, pharetra arcu eget, rutrum tellus. Integer id mollis sapien. Nulla aliquet vitae ipsum sed consequat. Mauris risus neque, hendrerit ut mauris a, aliquet venenatis ante. Donec sit amet ante et urna accumsan eleifend sed a ante. Fusce quis sem ac nisi mollis sodales. Mauris augue mauris, mattis nec justo sit amet, tincidunt feugiat ante. Maecenas hendrerit tellus sed nulla iaculis suscipit. Fusce sollicitudin sodales fermentum.',
        true,
        now() + make_interval(secs := 3),
        now() + make_interval(secs := 3));

INSERT INTO review (media_id, username, title, content, spoiler_free, modified_at, created_at)
values ((SELECT id FROM media WHERE title = 'Dune: Part One'),
        'user10',
        'A Masterpiece and a phenomenal adaptation.',
        'Aliquam sagittis mi eget metus tincidunt, id laoreet nunc accumsan. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Curabitur fringilla enim ut velit ultrices, eget ultrices turpis feugiat. Mauris in dictum nulla. Proin nisi elit, placerat id diam posuere, aliquam ornare risus. Sed eget augue eu erat tincidunt porttitor in aliquet odio. Proin malesuada eros vitae ex efficitur, ac tincidunt urna commodo. Integer ex sapien, scelerisque nec ornare at, maximus at massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas egestas risus id neque euismod sagittis.',
        true,
        now() + make_interval(secs := 4),
        now() + make_interval(secs := 4));

INSERT INTO review (media_id, username, title, content, spoiler_free, modified_at, created_at)
values ((SELECT id FROM media WHERE title = 'Dune: Part One'),
        'richard-rider',
        'Great looking but without depth',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam odio quam, fringilla eu consectetur eget, lacinia rutrum odio. Sed lobortis elementum congue. Cras at massa dolor. Morbi metus ex, euismod sit amet leo semper, elementum pharetra nisl. Curabitur pretium risus non nisi tristique, vitae finibus tellus pretium. Duis eu ultrices ante. Praesent massa ex, volutpat nec magna ac, vehicula sollicitudin quam. Integer vel est eget sapien finibus dignissim. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.',
        false,
        now() + make_interval(secs := 5),
        now() + make_interval(secs := 5));

INSERT INTO review (media_id, username, title, content, spoiler_free, modified_at, created_at)
values ((SELECT id FROM media WHERE title = 'Dune: Part One'),
        'emily-wokerson',
        'Great and well directed Adventure/Drama',
        'Cras lacinia ante at eros efficitur feugiat. In dapibus ut augue non laoreet. Etiam pharetra odio a magna placerat eleifend. Aenean et diam ac purus varius sodales ut vitae nulla. Cras consectetur pharetra tellus. Ut sodales elementum est et varius. In tincidunt lobortis urna eget feugiat. Phasellus sit amet mauris at metus cursus condimentum. Maecenas tristique odio nunc, vestibulum consequat ante auctor eu. Morbi id posuere orci. Quisque tempus lacinia nibh eget molestie. Pellentesque tristique bibendum consectetur. Pellentesque aliquet aliquet porttitor. Suspendisse interdum lacus sit amet velit egestas pellentesque.',
        false,
        now() + make_interval(secs := 6),
        now() + make_interval(secs := 6));
